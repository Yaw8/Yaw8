package com.billgate.backend.controller;

// Imports Category entity.
// Represents one category row from PostgreSQL.
import com.billgate.backend.entity.Category;

// Repository used to communicate with PostgreSQL.
import com.billgate.backend.repository.CategoryRepository;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

// Java List collection.
import java.util.List;

@RestController

// Base API route for categories.
//
// Example:
// /api/categories
@RequestMapping("/api/categories")
public class CategoryController {

    // Repository handles database operations.
    private final CategoryRepository categoryRepository;

    // Constructor injection.
    //
    // Spring automatically provides CategoryRepository.
    public CategoryController(
            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    // GET /api/categories
    //
    // Returns all categories from PostgreSQL.
    @GetMapping
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    // POST /api/categories
    //
    // Creates a new category in PostgreSQL.
    @PostMapping
    public Category createCategory(
            @RequestBody Category category
    ) {

        return categoryRepository.save(category);
    }

    // PUT /api/categories/{id}
    //
    // Updates an existing category.
    @PutMapping("/{id}")
    public Category updateCategory(
            @PathVariable Long id,
            @RequestBody Category updatedCategory
    ) {

        return categoryRepository.findById(id)

                .map(existingCategory -> {

                    // Update category fields.
                    existingCategory.setName(
                            updatedCategory.getName()
                    );

                    existingCategory.setType(
                            updatedCategory.getType()
                    );

                    // Save updated category.
                    return categoryRepository.save(
                            existingCategory
                    );
                })

                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found"
                        )
                );
    }

    // DELETE /api/categories/{id}
    //
    // Deletes category from PostgreSQL.
    @DeleteMapping("/{id}")
    public void deleteCategory(
            @PathVariable Long id
    ) {

        categoryRepository.deleteById(id);
    }
}
package com.billgate.backend.controller;

// Imports Category entity.
import com.billgate.backend.entity.Category;

// Imports User entity.
import com.billgate.backend.entity.User;

// Repository used for category database operations.
import com.billgate.backend.repository.CategoryRepository;

// Repository used to find users by id.
import com.billgate.backend.repository.UserRepository;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

// Java List collection.
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // Constructor injection.
    public CategoryController(
            CategoryRepository categoryRepository,
            UserRepository userRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // GET /api/categories?userId=1
    //
    // Returns ONLY categories that belong to one user.
    @GetMapping
    public List<Category> getCategoriesByUser(
            @RequestParam Long userId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return categoryRepository.findByUser(user);
    }

    // POST /api/categories?userId=1
    //
    // Creates category for a specific user.
    @PostMapping
    public Category createCategory(
            @RequestParam Long userId,
            @RequestBody Category category
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        category.setUser(user);

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
                    existingCategory.setName(updatedCategory.getName());
                    existingCategory.setType(updatedCategory.getType());

                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
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
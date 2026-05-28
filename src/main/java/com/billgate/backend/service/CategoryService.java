package com.billgate.backend.service;

// Category entity.
import com.billgate.backend.entity.Category;

// User entity.
import com.billgate.backend.entity.User;

// Category repository.
import com.billgate.backend.repository.CategoryRepository;

// Spring service annotation.
import org.springframework.stereotype.Service;

// Java List collection.
import java.util.List;

// Service layer for Category business logic.
//
// Controller should handle HTTP requests.
// Service should handle business/database logic.
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Constructor injection.
    public CategoryService(
            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    // Returns categories belonging to one user.
    public List<Category> getCategoriesForUser(
            User user
    ) {
        return categoryRepository.findByUser(user);
    }

    // Creates a category for one user.
    public Category createCategoryForUser(
            User user,
            Category category
    ) {
        category.setUser(user);

        return categoryRepository.save(category);
    }

    // Updates category name/type.
    public Category updateCategory(
            Long id,
            Category updatedCategory
    ) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(
                            updatedCategory.getName()
                    );

                    existingCategory.setType(
                            updatedCategory.getType()
                    );

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

    // Deletes category by id.
    public void deleteCategory(
            Long id
    ) {
        categoryRepository.deleteById(id);
    }
}
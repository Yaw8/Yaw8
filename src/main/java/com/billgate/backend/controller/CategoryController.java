package com.billgate.backend.controller;

// Imports Category entity.
// This represents one category record.
import com.billgate.backend.entity.Category;

// Imports User entity.
// Used to find the logged-in user.
import com.billgate.backend.entity.User;

// Imports CategoryService.
// Controller delegates category business logic to this service.
import com.billgate.backend.service.CategoryService;

// Repository used only for finding users.
// Category database logic is now handled by CategoryService.
import com.billgate.backend.repository.UserRepository;

// Spring Security context.
// This lets us read the authenticated user id
// that was placed there by JwtAuthenticationFilter.
import org.springframework.security.core.context.SecurityContextHolder;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

// Java List collection.
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    // Service handles category business/database logic.
    private final CategoryService categoryService;

    // UserRepository is used to find the logged-in user by id.
    private final UserRepository userRepository;

    // Constructor injection.
    //
    // Spring automatically provides:
    // - CategoryService
    // - UserRepository
    public CategoryController(
            CategoryService categoryService,
            UserRepository userRepository
    ) {
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    // Helper method.
    //
    // Gets logged-in user id from Spring Security.
    //
    // Flow:
    // JWT filter reads token
    // -> puts userId into SecurityContextHolder
    // -> controller reads userId here
    private Long getLoggedInUserId() {
        return (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // Helper method.
    //
    // Loads the logged-in User entity from database.
    private User getLoggedInUser() {
        Long userId = getLoggedInUserId();

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    // GET /api/categories
    //
    // Returns only categories owned by the logged-in user.
    @GetMapping
    public List<Category> getCategoriesByLoggedInUser() {
        User user = getLoggedInUser();

        return categoryService.getCategoriesForUser(user);
    }

    // POST /api/categories
    //
    // Creates a category for the logged-in user.
    @PostMapping
    public Category createCategory(
            @RequestBody Category category
    ) {
        User user = getLoggedInUser();

        return categoryService.createCategoryForUser(
                user,
                category
        );
    }

    // PUT /api/categories/{id}
    //
    // Updates an existing category.
    @PutMapping("/{id}")
    public Category updateCategory(
            @PathVariable Long id,
            @RequestBody Category updatedCategory
    ) {
        return categoryService.updateCategory(
                id,
                updatedCategory
        );
    }

    // DELETE /api/categories/{id}
    //
    // Deletes a category by id.
    //
    // Note:
    // If bills or repairs still use this category,
    // PostgreSQL will block the delete.
    @DeleteMapping("/{id}")
    public void deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
    }
}
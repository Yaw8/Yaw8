package com.billgate.backend.repository;

// Imports Category entity.
import com.billgate.backend.entity.Category;

// Imports User entity.
import com.billgate.backend.entity.User;

// Spring Data JPA repository.
import org.springframework.data.jpa.repository.JpaRepository;

// Java List collection.
import java.util.List;

// Repository for categories table.
public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    // Finds all categories belonging to one user.
    List<Category> findByUser(User user);
}
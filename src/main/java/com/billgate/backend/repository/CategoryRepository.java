package com.billgate.backend.repository;

import com.billgate.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository gives us database methods automatically.
// Example:
// - findAll()
// - findById()
// - save()
// - deleteById()
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
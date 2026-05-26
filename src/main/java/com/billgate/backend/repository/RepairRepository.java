package com.billgate.backend.repository;

// Imports Repair entity.
import com.billgate.backend.entity.Repair;

// Imports User entity.
import com.billgate.backend.entity.User;

// Spring Data JPA repository.
import org.springframework.data.jpa.repository.JpaRepository;

// Java List collection.
import java.util.List;

// Repository for repairs table.
public interface RepairRepository
        extends JpaRepository<Repair, Long> {

    // Finds all repairs belonging to one user.
    List<Repair> findByUser(User user);
}
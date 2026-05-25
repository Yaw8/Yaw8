package com.billgate.backend.repository;

// Imports User entity.
import com.billgate.backend.entity.User;

// Spring Data JPA repository.
import org.springframework.data.jpa.repository.JpaRepository;

// Repository for User table.
//
// JpaRepository automatically gives:
// - save()
// - findAll()
// - findById()
// - deleteById()
public interface UserRepository
        extends JpaRepository<User, Long> {

    // Finds user by email.
    //
    // Later used for login.
    User findByEmail(String email);
}
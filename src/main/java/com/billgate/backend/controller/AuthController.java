package com.billgate.backend.controller;

// Imports User entity.
import com.billgate.backend.entity.User;

// Imports UserRepository for database access.
import com.billgate.backend.repository.UserRepository;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

@RestController

// Base API route for authentication.
//
// Example:
// /api/auth/register
@RequestMapping("/api/auth")
public class AuthController {

    // Repository used to communicate with PostgreSQL.
    private final UserRepository userRepository;

    // Constructor injection.
    //
    // Spring automatically provides UserRepository.
    public AuthController(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    // POST /api/auth/register
    //
    // Creates a new user account.
    @PostMapping("/register")
    public User registerUser(
            @RequestBody User user
    ) {

        // Check whether email already exists.
        User existingUser =
                userRepository.findByEmail(
                        user.getEmail()
                );

        // Prevent duplicate accounts.
        if (existingUser != null) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // Save new user into PostgreSQL.
        return userRepository.save(user);
    }

    // POST /api/auth/login
    //
    // Verifies user login credentials.
    @PostMapping("/login")
    public User loginUser(
            @RequestBody User loginRequest
    ) {

        // Find user by email.
        User user =
                userRepository.findByEmail(
                        loginRequest.getEmail()
                );

        // Invalid email.
        if (user == null) {

            throw new RuntimeException(
                    "Invalid email"
            );
        }

        // Invalid password.
        //
        // Later we will replace this with
        // encrypted password hashing.
        if (!user.getPassword().equals(
                loginRequest.getPassword()
        )) {

            throw new RuntimeException(
                    "Invalid password"
            );
        }

        // Login successful.
        return user;
    }
}
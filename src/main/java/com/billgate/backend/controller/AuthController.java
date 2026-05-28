package com.billgate.backend.controller;

import jakarta.validation.Valid;

import com.billgate.backend.security.JwtUtil;
// Imports User entity.
import com.billgate.backend.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
private final BCryptPasswordEncoder passwordEncoder =
        new BCryptPasswordEncoder();
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
            @Valid @RequestBody User user
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
user.setPassword(
        passwordEncoder.encode(user.getPassword())
);
        // Save new user into PostgreSQL.
        return userRepository.save(user);
    }

    // POST /api/auth/login
    //
    // Verifies user login credentials.
@PostMapping("/login")
public String loginUser(
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

    // Compare typed password with hashed password.
    if (!passwordEncoder.matches(
            loginRequest.getPassword(),
            user.getPassword()
    )) {

        throw new RuntimeException(
                "Invalid password"
        );
    }

    // Generate JWT token after successful login.
    String token = JwtUtil.generateToken(
            user.getId(),
            user.getEmail()
    );

    // Return JWT token to Flutter.
    return token;
}
}
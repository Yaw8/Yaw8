package com.billgate.backend.controller;

// DTO for registration request.
// Contains name, email, password from Flutter.
import com.billgate.backend.dto.RegisterRequest;

// DTO for login request.
// Contains email and password from Flutter.
import com.billgate.backend.dto.LoginRequest;

// DTO returned after successful login.
// Contains JWT token only.
import com.billgate.backend.dto.AuthResponse;

// User entity.
// Represents saved user row in PostgreSQL.
import com.billgate.backend.entity.User;

// Repository used for user database operations.
import com.billgate.backend.repository.UserRepository;

// Utility used to create JWT tokens.
import com.billgate.backend.security.JwtUtil;

// Enables @Valid validation on request DTOs.
import jakarta.validation.Valid;

// BCrypt password encoder.
// Used to hash passwords and compare login passwords.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

@RestController

// Base API route for authentication.
//
// Public routes:
// - POST /api/auth/register
// - POST /api/auth/login
@RequestMapping("/api/auth")
public class AuthController {

    // Password encoder used for:
    // - hashing passwords during registration
    // - checking passwords during login
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // Repository used to communicate with PostgreSQL users table.
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
    //
    // Uses RegisterRequest DTO instead of User entity.
    @PostMapping("/register")
    public User registerUser(
            @Valid @RequestBody RegisterRequest request
    ) {

        // Check whether email already exists.
        User existingUser =
                userRepository.findByEmail(
                        request.email
                );

        // Prevent duplicate accounts.
        if (existingUser != null) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // Create new User entity manually from DTO.
        User user = new User();

        // Copy safe fields from request into entity.
        user.setName(request.name);
        user.setEmail(request.email);

        // Hash password before saving.
        user.setPassword(
                passwordEncoder.encode(
                        request.password
                )
        );

        // Save new user into PostgreSQL.
        return userRepository.save(user);
    }

    // POST /api/auth/login
    //
    // Verifies login credentials.
    //
    // Uses LoginRequest DTO instead of User entity.
    @PostMapping("/login")
    public AuthResponse loginUser(
            @Valid @RequestBody LoginRequest loginRequest
    ) {

        // Find user by email.
        User user =
                userRepository.findByEmail(
                        loginRequest.email
                );

        // Invalid email.
        if (user == null) {

            throw new RuntimeException(
                    "Invalid email"
            );
        }

        // Compare typed password with hashed password.
        if (!passwordEncoder.matches(
                loginRequest.password,
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

        // Return token inside DTO response.
        return new AuthResponse(token);
    }
}
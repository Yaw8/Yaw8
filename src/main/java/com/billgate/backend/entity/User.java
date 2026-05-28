package com.billgate.backend.entity;

// Jakarta persistence annotations for PostgreSQL entities.
import jakarta.persistence.*;

// Validation annotations.
//
// These automatically validate incoming data
// before saving into database.
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Lombok generates getters/setters automatically.
import lombok.Getter;
import lombok.Setter;

@Entity

// Maps this entity to PostgreSQL "users" table.
@Table(name = "users")

@Getter
@Setter
public class User {

    // Primary key for each user.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User's display name.
    //
    // Rules:
    // - cannot be empty
    // - minimum 2 characters
    @NotBlank(message = "Name is required")
    @Size(
            min = 2,
            message = "Name must be at least 2 characters"
    )
    private String name;

    // User email address.
    //
    // Rules:
    // - cannot be empty
    // - must match valid email format
    //
    // unique = true prevents duplicate emails
    // at database level.
    @NotBlank(message = "Email is required")

    @Email(message = "Invalid email format")

    @Column(unique = true)
    private String email;

    // User password.
    //
    // Rules:
    // - cannot be empty
    // - minimum 6 characters
    //
    // Passwords are now BCrypt hashed
    // before saving.
    @NotBlank(message = "Password is required")

    @Size(
            min = 6,
            message = "Password must be at least 6 characters"
    )
    private String password;
}
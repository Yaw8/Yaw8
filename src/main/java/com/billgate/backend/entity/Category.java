package com.billgate.backend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Category name.
    //
    // Examples:
    // - Insurance
    // - Utilities
    // - Plumbing
    private String name;
    
        // Category type.
    //
    // Examples:
    // - bill
    // - repair
    private String type;


     // The user who owns this category.
    //
    // Many categories can belong to one user.
    //
    // Example:
    // User -> many categories
    @ManyToOne
    // Foreign key column in PostgreSQL.
    @JoinColumn(name = "user_id")
    private User user;

}

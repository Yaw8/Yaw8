package com.billgate.backend.entity;

// JPA annotations for database mapping
import jakarta.persistence.*;

// Lombok generates getters/setters automatically
import lombok.Getter;
import lombok.Setter;

// Stores repair dates
import java.time.LocalDate;

/*
@Entity
Marks this class as a PostgreSQL table.
*/
@Entity

/*
Table name in PostgreSQL:
repairs
*/
@Table(name = "repairs")

@Getter
@Setter
public class Repair {

    /*
    Primary key
    */
    @Id

    /*
    Auto-incrementing ID
    */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Repair title
    // Example: "Brake Replacement"
    private String title;

    // Cost of the repair
    private Double cost;

    // pending, completed, etc.
    private String status;

    // Repair date
    private LocalDate repairDate;

    /*
    Many repairs can belong to one category.
    */
    @ManyToOne

    /*
    Foreign key column:
    category_id
    */
    @JoinColumn(name = "category_id")
    private Category category;

    // The user who owns this repair.
//
// Many repairs can belong to one user.

@ManyToOne
@JoinColumn(name = "user_id")
private User user;
}
package com.billgate.backend.entity;
// Import JPA annotations used to map Java objects to database tables. 
import jakarta.persistence.*;

// Lombok automatically generates getters/setters for us.
import lombok.Getter;
import lombok.Setter;

// Used for storing dates like due dates.
import java.time.LocalDate;

/*
@Etity
Tells hibernate/JPA:
"This Java class should becaome =a database table."
*/
@Entity

/*
@Table(name = "bills")
Soecufues the actual PostgreSQL table name. 
wthout this, JPA would try to guess the tale name. 
*/
@Table(name = "bills")

// Automatically creates getter methods. 
@Getter

// Automatically creates setter methods. 
@Setter 
public class Bill {
       /*
    @Id
    Marks this field as the PRIMARY KEY.
    Every table should have a unique identifier.
    */
    @Id

    /*
    @GeneratedValue
    Automatically increments IDs.
    PostgreSQL handles this for us.
    */
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Bill name like "Internet" or "water".
    private String name;

    // Amount due for the bill.
    private Double amountDue;

    // Monthly, yearly, one-time, etc. 
    private String recurrence;

    // paid, unpaid, overdue
    private String status;

    // Stores the due date.
    private LocalDate dueDate;

    /*
    Relationship:
    Many bills can belong to ONE category.

    Example:
    Utilities category:
      - Water bill
      - Internet bill
      - Electric bill

    All linked to one category.
    */
    @ManyToOne

    /*
    Creates the foreign key column in PostgreSQL:
    category_id
    */
    @JoinColumn(name = "category_id")
    private Category category;
}

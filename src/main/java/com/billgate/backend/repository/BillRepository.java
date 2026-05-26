package com.billgate.backend.repository;

// Imports Bill entity.
import com.billgate.backend.entity.Bill;

// Imports User entity.
import com.billgate.backend.entity.User;

// Spring Data JPA repository.
import org.springframework.data.jpa.repository.JpaRepository;

// Java List collection.
import java.util.List;

// Repository for bills table.
public interface BillRepository
        extends JpaRepository<Bill, Long> {

    // Finds all bills belonging to one user.
    List<Bill> findByUser(User user);
}
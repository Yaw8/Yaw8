package com.billgate.backend.repository;

// Imports the Repair entity
import com.billgate.backend.entity.Repair;

// Gives built-in database operations
import org.springframework.data.jpa.repository.JpaRepository;

/*
JpaRepository automatically provides:
- save()
- findAll()
- deleteById()
- findById()
*/
public interface RepairRepository extends JpaRepository<Repair, Long> {

}
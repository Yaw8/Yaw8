package com.billgate.backend.repository;

// Imports the Bill entity we created.
import com.billgate.backend.entity.Bill;

// JpaRepository gives us built-in database methods.
import org.springframework.data.jpa.repository.JpaRepository;

/*
This interface becomes the database access layer.

JpaRepository<Bill, Long>

Bill = entity/table type
Long = primary key type
*/
public interface BillRepository extends JpaRepository<Bill, Long> {

}
package com.billgate.backend.controller;

// Imports the Bill entity class.
// This represents one bill record from PostgreSQL.
import com.billgate.backend.entity.Bill;

// Imports the BillRepository.
// Repository handles database operations automatically.
import com.billgate.backend.repository.BillRepository;

// Imports Spring annotations used for REST APIs.
import org.springframework.web.bind.annotation.*;

// Imports Java List collection.
import java.util.List;

@RestController
// Base API route for all bill endpoints.
//
// Example:
// /api/bills
@RequestMapping("/api/bills")
public class BillController {

    // Repository used to communicate with PostgreSQL.
    //
    // Think of this as:
    // Controller -> Repository -> Database
    private final BillRepository billRepository;

    // Constructor injection.
    //
    // Spring automatically provides BillRepository here.
    public BillController(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    // GET /api/bills
    //
    // Returns all bills from PostgreSQL.
    //
    // Flutter calls this when loading Bills screen.
    @GetMapping
    public List<Bill> getAllBills() {

        return billRepository.findAll();
    }

    // POST /api/bills
    //
    // Receives JSON from Flutter
    // and saves a new bill into PostgreSQL.
    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {

        return billRepository.save(bill);
    }

    // PUT /api/bills/{id}
    //
    // Updates an existing bill.
    //
    // Example:
    // PUT /api/bills/1
    //
    // Flutter calls this when:
    // - editing bill
    // - toggling paid/unpaid
    @PutMapping("/{id}")
    public Bill updateBill(
            @PathVariable Long id,
            @RequestBody Bill updatedBill
    ) {

        // Find bill by id.
        return billRepository.findById(id)

                // If bill exists:
                .map(existingBill -> {

                    // Update fields with new values.
                    existingBill.setName(updatedBill.getName());

                    existingBill.setAmountDue(
                            updatedBill.getAmountDue()
                    );

                    existingBill.setRecurrence(
                            updatedBill.getRecurrence()
                    );

                    existingBill.setStatus(
                            updatedBill.getStatus()
                    );

                    existingBill.setDueDate(
                            updatedBill.getDueDate()
                    );

                    existingBill.setCategory(
                            updatedBill.getCategory()
                    );

                    // Save updated bill into PostgreSQL.
                    return billRepository.save(existingBill);
                })

                // If bill id does not exist:
                .orElseThrow(() ->
                        new RuntimeException("Bill not found")
                );
    }

    // DELETE /api/bills/{id}
    //
    // Deletes a bill from PostgreSQL.
    //
    // Example:
    // DELETE /api/bills/1
    //
    // Flutter calls this when user deletes a bill.
    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id) {

        billRepository.deleteById(id);
    }
}
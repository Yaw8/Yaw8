package com.billgate.backend.service;

// Imports Bill entity.
// Represents one bill row in PostgreSQL.
import com.billgate.backend.entity.Bill;

// Imports User entity.
// Used for bill ownership.
import com.billgate.backend.entity.User;

// Repository used for database operations on bills.
import com.billgate.backend.repository.BillRepository;

// Marks this class as a Spring Service.
//
// Spring automatically creates and manages this object.
import org.springframework.stereotype.Service;

// Java List collection.
import java.util.List;

// Service layer for Bill business logic.
//
// Architecture:
//
// Controller
// -> Service
// -> Repository
// -> Database
//
// Controllers should focus on HTTP requests.
// Services should focus on business logic.
@Service
public class BillService {

    // Repository used to access bills table.
    private final BillRepository billRepository;

    // Constructor injection.
    //
    // Spring automatically provides BillRepository.
    public BillService(
            BillRepository billRepository
    ) {
        this.billRepository = billRepository;
    }

    // Returns all bills belonging to one user.
    //
    // Used by:
    // GET /api/bills
    public List<Bill> getBillsForUser(
            User user
    ) {

        return billRepository.findByUser(user);
    }

    // Creates a new bill for one user.
    //
    // Used by:
    // POST /api/bills
    public Bill createBillForUser(
            User user,
            Bill bill
    ) {

        // Attach logged-in user ownership.
        bill.setUser(user);

        // Save into PostgreSQL.
        return billRepository.save(bill);
    }

    // Updates an existing bill.
    //
    // Used by:
    // PUT /api/bills/{id}
    public Bill updateBill(
            Long id,
            Bill updatedBill
    ) {

        // Find existing bill first.
        return billRepository.findById(id)

                // If bill exists:
                .map(existingBill -> {

                    // Update bill fields.
                    existingBill.setName(
                            updatedBill.getName()
                    );

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
                    return billRepository.save(
                            existingBill
                    );
                })

                // If bill id does not exist:
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bill not found"
                        )
                );
    }

    // Deletes a bill by id.
    //
    // Used by:
    // DELETE /api/bills/{id}
    public void deleteBill(
            Long id
    ) {

        billRepository.deleteById(id);
    }
}
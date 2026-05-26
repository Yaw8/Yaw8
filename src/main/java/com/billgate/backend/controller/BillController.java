package com.billgate.backend.controller;

// Imports Bill entity.
import com.billgate.backend.entity.Bill;

// Imports User entity.
import com.billgate.backend.entity.User;

// Repository used for bill database operations.
import com.billgate.backend.repository.BillRepository;

// Repository used to find users by id.
import com.billgate.backend.repository.UserRepository;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

// Java List collection.
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillRepository billRepository;
    private final UserRepository userRepository;

    // Constructor injection.
    public BillController(
            BillRepository billRepository,
            UserRepository userRepository
    ) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }

    // GET /api/bills?userId=1
    //
    // Returns ONLY bills belonging to one user.
    @GetMapping
    public List<Bill> getBillsByUser(
            @RequestParam Long userId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return billRepository.findByUser(user);
    }

    // POST /api/bills?userId=1
    //
    // Creates a new bill for a specific user.
    @PostMapping
    public Bill createBill(
            @RequestParam Long userId,
            @RequestBody Bill bill
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        bill.setUser(user);

        return billRepository.save(bill);
    }

    // PUT /api/bills/{id}
    //
    // Updates an existing bill.
    @PutMapping("/{id}")
    public Bill updateBill(
            @PathVariable Long id,
            @RequestBody Bill updatedBill
    ) {
        return billRepository.findById(id)
                .map(existingBill -> {
                    existingBill.setName(updatedBill.getName());
                    existingBill.setAmountDue(updatedBill.getAmountDue());
                    existingBill.setRecurrence(updatedBill.getRecurrence());
                    existingBill.setStatus(updatedBill.getStatus());
                    existingBill.setDueDate(updatedBill.getDueDate());
                    existingBill.setCategory(updatedBill.getCategory());

                    return billRepository.save(existingBill);
                })
                .orElseThrow(() ->
                        new RuntimeException("Bill not found")
                );
    }

    // DELETE /api/bills/{id}
    //
    // Deletes bill from PostgreSQL.
    @DeleteMapping("/{id}")
    public void deleteBill(
            @PathVariable Long id
    ) {
        billRepository.deleteById(id);
    }
}
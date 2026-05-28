package com.billgate.backend.controller;

// Imports Bill entity.
// Represents one bill record.
import com.billgate.backend.entity.Bill;

// Imports User entity.
// Used for logged-in ownership.
import com.billgate.backend.entity.User;

// Imports BillService.
// Service handles bill business logic.
import com.billgate.backend.service.BillService;

// Repository used only for loading users.
import com.billgate.backend.repository.UserRepository;

// Spring Security context.
//
// Lets us access authenticated user information
// placed there by JwtAuthenticationFilter.
import org.springframework.security.core.context.SecurityContextHolder;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

// Java List collection.
import java.util.List;

@RestController

// Base API route for bills.
@RequestMapping("/api/bills")
public class BillController {

    // Service layer for bill operations.
    private final BillService billService;

    // Repository used for loading logged-in user.
    private final UserRepository userRepository;

    // Constructor injection.
    //
    // Spring automatically provides:
    // - BillService
    // - UserRepository
    public BillController(
            BillService billService,
            UserRepository userRepository
    ) {
        this.billService = billService;
        this.userRepository = userRepository;
    }

    // Reads logged-in user id from Spring Security.
    //
    // Flow:
    // JWT filter
    // -> SecurityContextHolder
    // -> Controller
    private Long getLoggedInUserId() {

        return (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    // Loads full User entity for authenticated user.
    private User getLoggedInUser() {

        Long userId = getLoggedInUserId();

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }

    // GET /api/bills
    //
    // Returns bills belonging ONLY to logged-in user.
    @GetMapping
    public List<Bill> getAllBills() {

        User user = getLoggedInUser();

        return billService.getBillsForUser(user);
    }

    // POST /api/bills
    //
    // Creates a bill for logged-in user.
    @PostMapping
    public Bill createBill(
            @RequestBody Bill bill
    ) {

        User user = getLoggedInUser();

        return billService.createBillForUser(
                user,
                bill
        );
    }

    // PUT /api/bills/{id}
    //
    // Updates an existing bill.
    @PutMapping("/{id}")
    public Bill updateBill(
            @PathVariable Long id,
            @RequestBody Bill updatedBill
    ) {

        return billService.updateBill(
                id,
                updatedBill
        );
    }

    // DELETE /api/bills/{id}
    //
    // Deletes bill from PostgreSQL.
    @DeleteMapping("/{id}")
    public void deleteBill(
            @PathVariable Long id
    ) {

        billService.deleteBill(id);
    }
}
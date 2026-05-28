package com.billgate.backend.controller;

// Imports Repair entity.
// Represents one repair record.
import com.billgate.backend.entity.Repair;

// Imports User entity.
// Used for logged-in ownership.
import com.billgate.backend.entity.User;

// Imports RepairService.
// Service handles repair business logic.
import com.billgate.backend.service.RepairService;

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

// Base API route for repairs.
@RequestMapping("/api/repairs")
public class RepairController {

    // Service layer for repair operations.
    private final RepairService repairService;

    // Repository used for loading logged-in user.
    private final UserRepository userRepository;

    // Constructor injection.
    //
    // Spring automatically provides:
    // - RepairService
    // - UserRepository
    public RepairController(
            RepairService repairService,
            UserRepository userRepository
    ) {
        this.repairService = repairService;
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

    // GET /api/repairs
    //
    // Returns repairs belonging ONLY to logged-in user.
    @GetMapping
    public List<Repair> getAllRepairs() {

        User user = getLoggedInUser();

        return repairService.getRepairsForUser(user);
    }

    // POST /api/repairs
    //
    // Creates a repair for logged-in user.
    @PostMapping
    public Repair createRepair(
            @RequestBody Repair repair
    ) {

        User user = getLoggedInUser();

        return repairService.createRepairForUser(
                user,
                repair
        );
    }

    // PUT /api/repairs/{id}
    //
    // Updates an existing repair.
    @PutMapping("/{id}")
    public Repair updateRepair(
            @PathVariable Long id,
            @RequestBody Repair updatedRepair
    ) {

        return repairService.updateRepair(
                id,
                updatedRepair
        );
    }

    // DELETE /api/repairs/{id}
    //
    // Deletes repair from PostgreSQL.
    @DeleteMapping("/{id}")
    public void deleteRepair(
            @PathVariable Long id
    ) {

        repairService.deleteRepair(id);
    }
}
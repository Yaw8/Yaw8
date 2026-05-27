package com.billgate.backend.controller;

import com.billgate.backend.security.JwtUtil;

// Imports Repair entity.
import com.billgate.backend.entity.Repair;

// Imports User entity.
import com.billgate.backend.entity.User;

// Repository used for repair database operations.
import com.billgate.backend.repository.RepairRepository;

// Repository used to find users by id.
import com.billgate.backend.repository.UserRepository;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

// Java List collection.
import java.util.List;

@RestController
@RequestMapping("/api/repairs")
public class RepairController {

    private final RepairRepository repairRepository;
    private final UserRepository userRepository;

    // Constructor injection.
    public RepairController(
            RepairRepository repairRepository,
            UserRepository userRepository
    ) {
        this.repairRepository = repairRepository;
        this.userRepository = userRepository;
    }

    // Extracts logged-in user id from JWT token.
private Long extractUserIdFromHeader(
        String authHeader
) {

    // Removes "Bearer " from token header.
    String token =
            authHeader.replace("Bearer ", "");

    // Extracts userId from JWT token.
    return JwtUtil.extractUserId(token);
}
    // GET /api/repairs?userId=1
    //
    // Returns ONLY repairs belonging to one user.
    @GetMapping
    public List<Repair> getRepairsByUser(
            @RequestHeader("Authorization") String authHeader
    ) {
        Long userId = extractUserIdFromHeader(authHeader);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return repairRepository.findByUser(user);
    }

    // POST /api/repairs?userId=1
    //
    // Creates a new repair for a specific user.
   // POST /api/repairs
//
// Creates a new repair for the logged-in user.
// User is identified from JWT token, not from ?userId.
@PostMapping
public Repair createRepair(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody Repair repair
) {
    Long userId = extractUserIdFromHeader(authHeader);

    User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new RuntimeException("User not found")
            );

    repair.setUser(user);

    return repairRepository.save(repair);
}

    // PUT /api/repairs/{id}
    //
    // Updates an existing repair.
    @PutMapping("/{id}")
    public Repair updateRepair(
            @PathVariable Long id,
            @RequestBody Repair updatedRepair
    ) {
        return repairRepository.findById(id)
                .map(existingRepair -> {
                    existingRepair.setTitle(updatedRepair.getTitle());
                    existingRepair.setCost(updatedRepair.getCost());
                    existingRepair.setStatus(updatedRepair.getStatus());
                    existingRepair.setRepairDate(updatedRepair.getRepairDate());
                    existingRepair.setCategory(updatedRepair.getCategory());

                    return repairRepository.save(existingRepair);
                })
                .orElseThrow(() ->
                        new RuntimeException("Repair not found")
                );
    }

    // DELETE /api/repairs/{id}
    //
    // Deletes repair from PostgreSQL.
    @DeleteMapping("/{id}")
    public void deleteRepair(
            @PathVariable Long id
    ) {
        repairRepository.deleteById(id);
    }
}
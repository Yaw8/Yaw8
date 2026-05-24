package com.billgate.backend.controller;

// Imports Repair entity.
// Represents one repair row from PostgreSQL.
import com.billgate.backend.entity.Repair;

// Repository used to communicate with PostgreSQL.
import com.billgate.backend.repository.RepairRepository;

// Spring REST API annotations.
import org.springframework.web.bind.annotation.*;

// Java List collection.
import java.util.List;

@RestController

// Base API route for repairs.
//
// Example:
// /api/repairs
@RequestMapping("/api/repairs")
public class RepairController {

    // Repository handles database operations.
    private final RepairRepository repairRepository;

    // Constructor injection.
    //
    // Spring automatically provides RepairRepository.
    public RepairController(
            RepairRepository repairRepository
    ) {
        this.repairRepository = repairRepository;
    }

    // GET /api/repairs
    //
    // Returns all repairs from PostgreSQL.
    @GetMapping
    public List<Repair> getAllRepairs() {

        return repairRepository.findAll();
    }

    // POST /api/repairs
    //
    // Creates a new repair in PostgreSQL.
    @PostMapping
    public Repair createRepair(
            @RequestBody Repair repair
    ) {

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

 // Update repair fields.
existingRepair.setTitle(
        updatedRepair.getTitle()
);

existingRepair.setCost(
        updatedRepair.getCost()
);

existingRepair.setStatus(
        updatedRepair.getStatus()
);

existingRepair.setRepairDate(
        updatedRepair.getRepairDate()
);

existingRepair.setCategory(
        updatedRepair.getCategory()
);

existingRepair.setCategory(
        updatedRepair.getCategory()
);
                    existingRepair.setCategory(
                            updatedRepair.getCategory()
                    );

                    // Save updated repair.
                    return repairRepository.save(
                            existingRepair
                    );
                })

                .orElseThrow(() ->
                        new RuntimeException(
                                "Repair not found"
                        )
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
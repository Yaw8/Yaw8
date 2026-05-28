package com.billgate.backend.service;

// Imports Repair entity.
// Represents one repair row in PostgreSQL.
import com.billgate.backend.entity.Repair;

// Imports User entity.
// Used for repair ownership.
import com.billgate.backend.entity.User;

// Repository used for repair database operations.
import com.billgate.backend.repository.RepairRepository;

// Marks this class as a Spring Service.
//
// Spring automatically creates/manages this object.
import org.springframework.stereotype.Service;

// Java List collection.
import java.util.List;

// Service layer for Repair business logic.
//
// Architecture:
//
// Controller
// -> Service
// -> Repository
// -> Database
//
// Controllers handle HTTP requests.
// Services handle business/database logic.
@Service
public class RepairService {

    // Repository used to access repairs table.
    private final RepairRepository repairRepository;

    // Constructor injection.
    //
    // Spring automatically provides RepairRepository.
    public RepairService(
            RepairRepository repairRepository
    ) {
        this.repairRepository = repairRepository;
    }

    // Returns all repairs belonging to one user.
    //
    // Used by:
    // GET /api/repairs
    public List<Repair> getRepairsForUser(
            User user
    ) {

        return repairRepository.findByUser(user);
    }

    // Creates a repair for one user.
    //
    // Used by:
    // POST /api/repairs
    public Repair createRepairForUser(
            User user,
            Repair repair
    ) {

        // Attach logged-in user ownership.
        repair.setUser(user);

        // Save repair into PostgreSQL.
        return repairRepository.save(repair);
    }

    // Updates an existing repair.
    //
    // Used by:
    // PUT /api/repairs/{id}
    public Repair updateRepair(
            Long id,
            Repair updatedRepair
    ) {

        // Find existing repair first.
        return repairRepository.findById(id)

                // If repair exists:
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

                    // Save updated repair into PostgreSQL.
                    return repairRepository.save(
                            existingRepair
                    );
                })

                // If repair id does not exist:
                .orElseThrow(() ->
                        new RuntimeException(
                                "Repair not found"
                        )
                );
    }

    // Deletes repair by id.
    //
    // Used by:
    // DELETE /api/repairs/{id}
    public void deleteRepair(
            Long id
    ) {

        repairRepository.deleteById(id);
    }
}
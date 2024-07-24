package com.example.project1.repository;

import com.example.project1.entities.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, UUID> {

    @Query("FROM Reimbursement r WHERE r.user.userId = :userId")
    List<Reimbursement> findAllByUserId(@Param("userId") UUID userId);

    @Query("FROM Reimbursement r WHERE r.user.userId = :userId AND r.status = 'PENDING'")
    List<Reimbursement> findAllByUserIDAndStatus(UUID userId);

    @Query("FROM Reimbursement r WHERE r.status = 'PENDING'")
    List<Reimbursement> findAllByStatus();
}

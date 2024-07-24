package com.example.project1.service;

import com.example.project1.entities.Reimbursement;
import com.example.project1.entities.User;
import com.example.project1.exception.CustomException;
import com.example.project1.repository.ReimbursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReimbursementService {

    private final ReimbursementRepository reimbursementRepository;
    private final UserService userService;

    @Autowired
    public ReimbursementService(ReimbursementRepository rs, UserService userService) {
        this.reimbursementRepository = rs;
        this.userService = userService;
    }

    public Reimbursement createReimbursement(Reimbursement reimbursement) throws CustomException {
        if (reimbursement.getUser() == null || reimbursement.getAmount() <= 0) {
            throw new CustomException("Enter valid parameter");
        }
        // No need to store value, because if user doesn't exist it will throw an error
        User u = this.userService.getUserById(reimbursement.getUser().getUserId());
        reimbursement.setUser(u);
        return this.reimbursementRepository.save(reimbursement);
    }

    public List<Reimbursement> getReimbursementsByUser(UUID userId) throws CustomException {
        // No need to store value, because if user doesn't exist it will throw an error
        this.userService.getUserById(userId);
        return this.reimbursementRepository.findAllByUserId(userId);
    }

    public List<Reimbursement> getPendingReimbursementsByUser(UUID userId) throws CustomException {
        // No need to store value, because if user doesn't exist it will throw an error
        this.userService.getUserById(userId);
        return this.reimbursementRepository.findAllByUserIDAndStatus(userId);
    }

    public List<Reimbursement> getAllReimbursements(UUID managerId) throws CustomException {
        // No need to store value, because if user isn't manager it will throw an error
        this.userService.isIdManagerId(managerId);
        return this.reimbursementRepository.findAll();
    }

    public List<Reimbursement> getAllPendingReimbursements(UUID managerId) throws CustomException {
        // No need to store value, because if user isn't manager it will throw an error
        this.userService.isIdManagerId(managerId);
        return this.reimbursementRepository.findAllByStatus();
    }

    public Reimbursement updateDescription(UUID rId, String description) throws CustomException {
        Optional<Reimbursement> r = this.reimbursementRepository.findById(rId);
        if (r.isEmpty()) {
            throw new CustomException("No reimbursement with id: " + rId);
        }
        Reimbursement re = r.get();
        if (!re.getStatus().equals("PENDING")) {
            throw new CustomException("Reimbursement cannot be edited because it is resolved.");
        }
        re.setDescription(description);
        return this.reimbursementRepository.save(re);
    }

    public Reimbursement resolveReimbursement(UUID rId, String status) throws CustomException {
        Optional<Reimbursement> r = this.reimbursementRepository.findById(rId);
        if (r.isEmpty()) {
            throw new CustomException("No reimbursement with id: " + rId);
        }
        Reimbursement re = r.get();
        re.setStatus(status);
        return this.reimbursementRepository.save(re);
    }
}

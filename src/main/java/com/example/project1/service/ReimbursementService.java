package com.example.project1.service;

import com.example.project1.entities.Reimbursement;
import com.example.project1.entities.User;
import com.example.project1.exception.CustomException;
import com.example.project1.repository.ReimbursementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReimbursementService {

    private final ReimbursementRepository reimbursementRepository;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ReimbursementService.class);


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
        logger.info("Reimbursement just created for amount of {} by {}", reimbursement.getAmount(), reimbursement.getUser());
        return this.reimbursementRepository.save(reimbursement);
    }

    public List<Reimbursement> getReimbursementsByUser(UUID userId) throws CustomException {
        // No need to store value, because if user doesn't exist it will throw an error
        this.userService.getUserById(userId);
        logger.info("Retrieved all reimbursement by user: {}", userId);
        return this.reimbursementRepository.findAllByUserId(userId);
    }

    public List<Reimbursement> getPendingReimbursementsByUser(UUID userId) throws CustomException {
        // No need to store value, because if user doesn't exist it will throw an error
        this.userService.getUserById(userId);
        logger.info("Retrieved all pending reimbursement by user: {}", userId);
        return this.reimbursementRepository.findAllByUserIDAndStatus(userId);
    }

    public List<Reimbursement> getAllReimbursements(UUID managerId) throws CustomException {
        // No need to store value, because if user isn't manager it will throw an error
        this.userService.isIdManagerId(managerId);
        logger.info("Retrieved all reimbursement by all users for manager: {}", managerId);
        return this.reimbursementRepository.findAll();
    }

    public List<Reimbursement> getAllPendingReimbursements(UUID managerId) throws CustomException {
        // No need to store value, because if user isn't manager it will throw an error
        this.userService.isIdManagerId(managerId);
        logger.info("Retrieved all pending reimbursement by all users for manager: {}", managerId);
        return this.reimbursementRepository.findAllByStatus();
    }

    public Reimbursement updateDescription(Reimbursement rei, User user) throws CustomException {
        Optional<Reimbursement> r = this.reimbursementRepository.findById(rei.getReimId());
        if (r.isEmpty()) {
            throw new CustomException("No reimbursement with id: " + rei.getReimId());
        }
        Reimbursement re = r.get();
        if (! re.getUser().equals(user)) {
            throw new CustomException("Cannot edit someone else's reimbursement");
        }
        if (!re.getStatus().equals("PENDING")) {
            throw new CustomException("Resolved reimbursement cannot be edited.");
        }
        re.setDescription(rei.getDescription());
        logger.info("Description updated for reimbursement id: {}", rei.getReimId());
        return this.reimbursementRepository.save(re);
    }

    public Reimbursement resolveReimbursement(UUID rId, String status, UUID user) throws CustomException {
        userService.isIdManagerId(user);
        Optional<Reimbursement> r = this.reimbursementRepository.findById(rId);
        if (r.isEmpty()) {
            throw new CustomException("No reimbursement with id: " + rId);
        }
        Reimbursement re = r.get();
        re.setStatus(status);
        logger.info("Reimbursement id: {}  resolved by {}", rId, user);
        return this.reimbursementRepository.save(re);
    }
}

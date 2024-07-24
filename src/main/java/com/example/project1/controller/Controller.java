package com.example.project1.controller;

import com.example.project1.entities.Reimbursement;
import com.example.project1.entities.User;
import com.example.project1.exception.CustomException;
import com.example.project1.service.ReimbursementService;
import com.example.project1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class Controller {

    private final ReimbursementService reimbursementService;
    private final UserService userService;

    @Autowired
    public Controller(ReimbursementService reimbursementService, UserService userService) {
        this.reimbursementService = reimbursementService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User u = this.userService.createUser(user);
            return ResponseEntity.status(200).body(u);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam UUID managerId) {
        try {
            List<User> r = this.userService.getAllUsers(managerId);
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

//    Extra
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        try {
             User user = this.userService.getUserById(id);
             return ResponseEntity.ok().body(user);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/reimbursement")
    public ResponseEntity<?> createReimbursement(@RequestBody Reimbursement reimbursement) {
        try {
            Reimbursement r = this.reimbursementService.createReimbursement(reimbursement);
            return ResponseEntity.status(201).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/reimbursement/{userId}")
    public ResponseEntity<?> getReimbursementsByUser(@PathVariable UUID userId) {
        try {
            List<Reimbursement> r = this.reimbursementService.getReimbursementsByUser(userId);
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/reimbursement/pending/{userId}")
    public ResponseEntity<?> getPendingReimbursementsByUser(@PathVariable UUID userId) {
        try {
            List<Reimbursement> r = this.reimbursementService.getPendingReimbursementsByUser(userId);
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // TODO: when finished login functionality remove manager_id from pathvariable
    @GetMapping("/reimbursements/all")
    public ResponseEntity<?> getAllReimbursements(@RequestParam UUID managerId) {
        try {
            List<Reimbursement> r = this.reimbursementService.getAllReimbursements(managerId);
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/reimbursements/pending/all")
    public ResponseEntity<?> getAllPendingReimbursements(@RequestParam UUID managerId) {
        try {
            List<Reimbursement> r = this.reimbursementService.getAllPendingReimbursements(managerId);
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID userId, @RequestParam UUID managerId) {
        try {
            this.userService.deleteUserById(userId, managerId);
            return ResponseEntity.status(200).body("User successfully deleted");
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/promote/{userId}")
    public ResponseEntity<?> promote(@PathVariable UUID userId, @RequestParam UUID managerId) {
        try {
            User user = this.userService.promoteById(userId, managerId);
            return ResponseEntity.ok().body(user);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}

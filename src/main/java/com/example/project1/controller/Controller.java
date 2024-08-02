package com.example.project1.controller;

import com.example.project1.entities.Reimbursement;
import com.example.project1.entities.User;
import com.example.project1.exception.CustomException;
import com.example.project1.service.AuthService;
import com.example.project1.service.ReimbursementService;
import com.example.project1.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

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
    public ResponseEntity<?> getAllUsers(HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            List<User> r = this.userService.getAllUsers(currentUser.getUserId());
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserById(HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
             User user = this.userService.getUserById(currentUser.getUserId());
             return ResponseEntity.ok().body(user);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/reimbursement")
    public ResponseEntity<?> createReimbursement(@RequestBody Reimbursement reimbursement, HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            reimbursement.setUser(currentUser);
            Reimbursement r = this.reimbursementService.createReimbursement(reimbursement);
            return ResponseEntity.status(201).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/user/reimbursement")
    public ResponseEntity<?> getReimbursementsByUser(HttpSession httpSession) {
        try {
            if (httpSession == null) {
                System.out.println("http sessison is null");

            }
            User currentUser = (User) httpSession.getAttribute("user");
            System.out.print(currentUser);
            if (currentUser == null) {
                System.out.println("user not found in session");
                return ResponseEntity.status(400).body("Login first");
            }
            List<Reimbursement> r = this.reimbursementService.getReimbursementsByUser(currentUser.getUserId());
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("user/reimbursement/pending")
    public ResponseEntity<?> getPendingReimbursementsByUser(HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            List<Reimbursement> r = this.reimbursementService.getPendingReimbursementsByUser(currentUser.getUserId());
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/reimbursements/all")
    public ResponseEntity<?> getAllReimbursements(HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            List<Reimbursement> r = this.reimbursementService.getAllReimbursements(currentUser.getUserId());
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/reimbursements/pending/all")
    public ResponseEntity<?> getAllPendingReimbursements(HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            List<Reimbursement> r = this.reimbursementService.getAllPendingReimbursements(currentUser.getUserId());
            return ResponseEntity.status(200).body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID userId, HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            this.userService.deleteUserById(userId, currentUser.getUserId());
            return ResponseEntity.status(200).body("User successfully deleted");
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/promote/{userId}")
    public ResponseEntity<?> promote(@PathVariable UUID userId, HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            User user = this.userService.promoteById(userId, currentUser.getUserId());
            return ResponseEntity.ok().body(user);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/reimbursements/description")
    public ResponseEntity<?> updateDescription(@RequestParam UUID reimbursement_id, @RequestBody String description, HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            Reimbursement r = this.reimbursementService.updateDescription(reimbursement_id, description, currentUser);
            return ResponseEntity.ok().body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/reimbursements/resolve/{status}")
    public ResponseEntity<?> resolveReimbursement(@RequestParam UUID reimbursement_id, @PathVariable String status, HttpSession httpSession) {
        try {
            User currentUser = (User) httpSession.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(400).body("Login first");
            }
            Reimbursement r = this.reimbursementService.resolveReimbursement(reimbursement_id, status, currentUser.getUserId());
            return ResponseEntity.ok().body(r);
        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}

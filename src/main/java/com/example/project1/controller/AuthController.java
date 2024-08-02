package com.example.project1.controller;

import com.example.project1.entities.LoginDTO;
import com.example.project1.entities.User;
import com.example.project1.exception.CustomException;
import com.example.project1.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public static HttpSession ses;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpSession httpSession) {
        try {
            User loggedInUser = authService.logIn(loginDTO);

            if (loggedInUser != null) {
                httpSession.setAttribute("user", loggedInUser);
                ses = httpSession;
                return ResponseEntity.ok().body(loggedInUser);
            }
            else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

        } catch (CustomException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity.ok().body("Logged out");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(HttpSession httpSession) {
        User currentUser = (User) httpSession.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(400).body("Login first");
        }
        return ResponseEntity.ok().body(currentUser);
    }



}

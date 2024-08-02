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
                System.out.println("logged in");
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



}

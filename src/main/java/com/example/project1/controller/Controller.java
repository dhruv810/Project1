package com.example.project1.controller;

import com.example.project1.service.ReimbursementService;
import com.example.project1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final ReimbursementService reimbursementService;
    private final UserService userService;

    @Autowired
    public Controller(ReimbursementService reimbursementService, UserService userService) {
        this.reimbursementService = reimbursementService;
        this.userService = userService;
    }

}

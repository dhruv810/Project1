package com.example.project1.service;

import com.example.project1.entities.LoginDTO;
import com.example.project1.entities.User;
import com.example.project1.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User logIn(LoginDTO lDTO) throws CustomException {
        if (lDTO.getUsername().isEmpty() || lDTO.getPassword().isEmpty()) {
            throw new CustomException("Enter Valid username and Password");
        }
        logger.info("{} just logged in.", lDTO.getUsername());
        return this.userService.getUserByUsernameAndPassword(lDTO.getUsername(), lDTO.getPassword());
    }
}

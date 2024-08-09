package com.example.project1.service;

import com.example.project1.entities.Reimbursement;
import com.example.project1.entities.User;
import com.example.project1.exception.CustomException;
import com.example.project1.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.ConnectIOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(ReimbursementService.class);

    public User getUserByUsernameAndPassword(String username, String password) throws CustomException{
        Optional<User> user = this.userRepository.findByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            throw new CustomException("Enter Valid username and Password");
        }
        logger.info("Verified user: {}  with username and password", username);
        return user.get();
    }

    public User createUser(User user) throws CustomException {
        if (user.getUsername().length() < 5 || user.getPassword().length() < 5) {
            throw new CustomException("Username and password must be longer than 5 characters.");
        }
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            throw new CustomException("First and last name cannot be empty.");
        }
        if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }
        logger.info("Created new user with username: {}", user.getUsername());
        return this.userRepository.save(user);
    }

    public User getUserById(UUID id) throws CustomException {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new CustomException("No user with userId: " + id);
        }
        logger.info("Retrieved user with user id: {} ", id);
        return user.get();
    }

    public void isIdManagerId(UUID id) throws CustomException {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new CustomException("No user with userId: " + id);
        }
        if (user.get().getRole().equals("MANAGER")) {
            return;
        }
        logger.info("Checked if id: {} belong to a manager.", id);
        throw new CustomException("You are not a manager");
    }

    public List<User> getAllUsers(UUID managerId) throws CustomException {
        this.isIdManagerId(managerId);
        logger.info("manager: {} retrieved all the user info", managerId);
        return this.userRepository.findAll();
    }

    public void deleteUserById(UUID userId, UUID managerId) throws CustomException {
        this.isIdManagerId(managerId);
        logger.info("manager: {} deleted user: {}", managerId, userId);
        this.userRepository.deleteById(userId);
    }

    public User promoteById(UUID userId, UUID managerId) throws CustomException {
        this.isIdManagerId(managerId);
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new CustomException("No user with userId: " + userId);
        }
        User u = user.get();
        u.setRole("MANAGER");
        logger.info("manager: {}  promoted employee: {}", managerId, userId);
        return this.userRepository.save(u);
    }

}

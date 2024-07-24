package com.example.project1.service;

import com.example.project1.entities.Reimbursement;
import com.example.project1.entities.User;
import com.example.project1.exception.CustomException;
import com.example.project1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User createUser(User user) throws CustomException {
        if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new CustomException("Username already exists");
        }
        return this.userRepository.save(user);
    }

    public User getUserById(UUID id) throws CustomException {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new CustomException("No user with userId: " + id);
        }
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
        throw new CustomException("You are not a manager");
    }

    public List<User> getAllUsers(UUID managerId) throws CustomException {
        this.isIdManagerId(managerId);
        return this.userRepository.findAll();
    }

    public void deleteUserById(UUID userId, UUID managerId) throws CustomException {
        this.isIdManagerId(managerId);
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
        return this.userRepository.save(u);
    }
}

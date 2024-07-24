package com.example.project1.service;

import com.example.project1.repository.ReimbursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReimbursementService {

    private final ReimbursementRepository reimbursementRepository;

    @Autowired
    public ReimbursementService(ReimbursementRepository rs) {
        this.reimbursementRepository = rs;
    }

}

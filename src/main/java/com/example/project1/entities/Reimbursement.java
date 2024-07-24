package com.example.project1.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "reimbursements")
public class Reimbursement {

    @Id
    @Column(name = "reim_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reimId;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    public Reimbursement() {
    }

    public Reimbursement(String description, Double amount, String status, User user) {
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.user = user;
    }

    public Reimbursement(UUID reimId, String description, Double amount, String status, User user) {
        this.reimId = reimId;
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.user = user;
    }

    public UUID getReimId() {
        return reimId;
    }

    public void setReimId(UUID reimId) {
        this.reimId = reimId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reimbursement that = (Reimbursement) o;
        return Objects.equals(reimId, that.reimId) && Objects.equals(description, that.description) && Objects.equals(amount, that.amount) && Objects.equals(status, that.status) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimId, description, amount, status, user);
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "reimId=" + reimId +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", userId=" + user +
                '}';
    }
}

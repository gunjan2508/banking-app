package com.bank.banking_app.account;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String name;
    private String email;
    private String phone;
    private BigDecimal balance;
    private String accountType;
    private String status;
    private LocalDateTime createdAt;

    public Account() {}

    // Getters
    public Long getAccountId() { return accountId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public BigDecimal getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
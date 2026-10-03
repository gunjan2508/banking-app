package com.bank.banking_app.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponseDTO {

    private Long accountId;
    private String name;
    private String email;
    private String phone;
    private BigDecimal balance;
    private String accountType;
    private String status;
    private LocalDateTime createdAt;
    private String accountNumber;

    public AccountResponseDTO() {}

    public AccountResponseDTO(Long accountId, String name, String email,
                              String phone, BigDecimal balance, String accountType,
                              String status, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getAccountId() { return accountId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public BigDecimal getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getAccountNumber() { return accountNumber; }

    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
}
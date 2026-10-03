package com.bank.banking_app.fixeddeposit;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fixed_deposits")
public class FixedDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;
    private BigDecimal principal;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal maturityAmount;
    private LocalDate maturityDate;
    private String status;
    private LocalDateTime createdAt;

    public FixedDeposit() {}

    public Long getId() { return id; }
    public Long getAccountId() { return accountId; }
    public BigDecimal getPrincipal() { return principal; }
    public BigDecimal getInterestRate() { return interestRate; }
    public Integer getTenureMonths() { return tenureMonths; }
    public BigDecimal getMaturityAmount() { return maturityAmount; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }
    public void setMaturityAmount(BigDecimal maturityAmount) { this.maturityAmount = maturityAmount; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
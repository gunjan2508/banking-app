package com.bank.banking_app.loan;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    private Long accountId;
    private String loanType;
    private BigDecimal amount;
    private BigDecimal emi;
    private Integer tenure;
    private String status;
    private LocalDateTime appliedAt;

    public Loan() {}

    public Long getLoanId() { return loanId; }
    public Long getAccountId() { return accountId; }
    public String getLoanType() { return loanType; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getEmi() { return emi; }
    public Integer getTenure() { return tenure; }
    public String getStatus() { return status; }
    public LocalDateTime getAppliedAt() { return appliedAt; }

    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setLoanType(String loanType) { this.loanType = loanType; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setEmi(BigDecimal emi) { this.emi = emi; }
    public void setTenure(Integer tenure) { this.tenure = tenure; }
    public void setStatus(String status) { this.status = status; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
package com.bank.banking_app.transaction;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String type;
    private String status;
    private String description;
    private String referenceNumber;
    private LocalDateTime date;

    public Transaction() {}

    public Long getTransactionId() { return transactionId; }
    public Long getFromAccountId() { return fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public String getReferenceNumber() { return referenceNumber; }
    public LocalDateTime getDate() { return date; }

    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
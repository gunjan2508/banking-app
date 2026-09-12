package com.bank.banking_app.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {

    private Long transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String type;
    private String status;
    private String description;
    private String referenceNumber;
    private LocalDateTime date;

    public TransactionResponseDTO() {}

    public TransactionResponseDTO(Long transactionId, Long fromAccountId, Long toAccountId,
                                  BigDecimal amount, String type, String status,
                                  String description, String referenceNumber, LocalDateTime date) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.description = description;
        this.referenceNumber = referenceNumber;
        this.date = date;
    }

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
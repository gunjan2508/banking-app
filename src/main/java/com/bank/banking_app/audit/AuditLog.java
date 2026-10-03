package com.bank.banking_app.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String action;
    private String details;
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(String username, String action, String details) {
        this.username = username;
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setAction(String action) { this.action = action; }
    public void setDetails(String details) { this.details = details; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
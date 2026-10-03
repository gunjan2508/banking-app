package com.bank.banking_app.security;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String role;
    private boolean locked;
    private int failedAttempts;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;

    public User() {}

    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isLocked() { return locked; }
    public int getFailedAttempts() { return failedAttempts; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
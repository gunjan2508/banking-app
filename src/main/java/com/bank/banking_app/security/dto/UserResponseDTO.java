package com.bank.banking_app.security.dto;

import java.time.LocalDateTime;

public class UserResponseDTO {

    private Long userId;
    private String username;
    private String email;
    private String role;
    private boolean locked;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public UserResponseDTO(
            Long userId,
            String username,
            String email,
            String role,
            boolean locked,
            LocalDateTime createdAt,
            LocalDateTime lastLogin
    ) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.locked = locked;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isLocked() {
        return locked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }}
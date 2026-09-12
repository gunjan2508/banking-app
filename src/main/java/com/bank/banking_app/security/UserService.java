package com.bank.banking_app.security;

import com.bank.banking_app.security.dto.UserResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final int MAX_ATTEMPTS = 5;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        validatePassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("CUSTOMER");
        user.setLocked(false);
        user.setFailedAttempts(0);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return "Registration successful! Welcome to NeoVault.";
    }

    private void validatePassword(String password) {
        if (password.length() < 8)
            throw new RuntimeException("Password must be at least 8 characters");
        if (!Pattern.compile("[A-Z]").matcher(password).find())
            throw new RuntimeException("Password must contain at least one uppercase letter");
        if (!Pattern.compile("[0-9]").matcher(password).find())
            throw new RuntimeException("Password must contain at least one number");
        if (!Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]").matcher(password).find())
            throw new RuntimeException("Password must contain at least one special character");
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isLocked()) {
            throw new RuntimeException("Account locked. Contact support.");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if (user.getFailedAttempts() >= MAX_ATTEMPTS) {
                user.setLocked(true);
                userRepository.save(user);
                throw new RuntimeException("Account locked after 5 failed attempts.");
            }
            userRepository.save(user);
            throw new RuntimeException("Invalid password. " + (MAX_ATTEMPTS - user.getFailedAttempts()) + " attempts remaining.");
        }
        user.setFailedAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return jwtUtil.generateToken(username);
    }

    public String changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        validatePassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password changed successfully";
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserResponseDTO> getAllUsersDTO() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail() != null ? user.getEmail() : "",
                        user.getRole(),
                        user.isLocked(),
                        user.getCreatedAt(),
                        user.getLastLogin()
                ))
                .toList();
    }

    public String unlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLocked(false);
        user.setFailedAttempts(0);
        userRepository.save(user);
        return "User unlocked successfully";
    }

    public String changeRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        userRepository.save(user);
        return "Role updated to " + role;
    }
}
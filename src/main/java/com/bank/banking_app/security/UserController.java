package com.bank.banking_app.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.bank.banking_app.security.dto.UserResponseDTO;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.register(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String token = userService.login(request.get("username"), request.get("password"));
            User user = userService.getUserByUsername(request.get("username"));
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", user.getRole(),
                    "username", user.getUsername(),
                    "lastLogin", user.getLastLogin() != null ? user.getLastLogin().toString() : "First login"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(Map.of(
                    "username", user.getUsername(),
                    "role", user.getRole(),
                    "email", user.getEmail() != null ? user.getEmail() : "",
                    "lastLogin", user.getLastLogin() != null ? user.getLastLogin().toString() : "First login",
                    "createdAt", user.getCreatedAt().toString(),
                    "locked", user.isLocked()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ADMIN ONLY
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsersDTO());
    }

    @PutMapping("/admin/unlock/{userId}")
    public ResponseEntity<String> unlockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.unlockUser(userId));
    }

    @PutMapping("/admin/role/{userId}")
    public ResponseEntity<String> changeRole(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(userService.changeRole(userId, request.get("role")));
    }
}

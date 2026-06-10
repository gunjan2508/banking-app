package com.bank.banking_app.notification;

import com.bank.banking_app.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;
    private final com.bank.banking_app.security.UserRepository userRepository;

    public NotificationController(NotificationService notificationService, JwtUtil jwtUtil,
                                  com.bank.banking_app.security.UserRepository userRepository) {
        this.notificationService = notificationService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    private Long getUserId(String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUserId();
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(notificationService.getNotifications(getUserId(authHeader)));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(Map.of("count", notificationService.getUnreadCount(getUserId(authHeader))));
    }

    @PutMapping("/mark-read")
    public ResponseEntity<String> markAllRead(@RequestHeader("Authorization") String authHeader) {
        notificationService.markAllRead(getUserId(authHeader));
        return ResponseEntity.ok("All notifications marked as read");
    }
}
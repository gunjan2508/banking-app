package com.bank.banking_app.audit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<AuditLog>> getLogsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(auditLogService.getLogsByUsername(username));
    }
}
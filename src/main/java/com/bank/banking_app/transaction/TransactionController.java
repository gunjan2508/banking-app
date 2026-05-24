package com.bank.banking_app.transaction;

import com.bank.banking_app.transaction.dto.TransactionRequestDTO;
import com.bank.banking_app.transaction.dto.TransactionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // POST /transactions/deposit
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@Valid @RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.deposit(request));
    }

    // POST /transactions/withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@Valid @RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.withdraw(request));
    }

    // POST /transactions/transfer
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }

    // GET /transactions/history/{accountId}
    @GetMapping("/history/{accountId}")
    public ResponseEntity<Page<TransactionResponseDTO>> getHistory(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId, page, size));
    }

    // GET /transactions/statement/{accountId}
    @GetMapping("/statement/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getStatement(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getAccountStatement(accountId));
    }

    // GET /transactions/filter/{accountId}?start=2026-01-01T00:00:00&end=2026-12-31T23:59:59
    @GetMapping("/filter/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getByDateRange(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(accountId, start, end));
    }
}
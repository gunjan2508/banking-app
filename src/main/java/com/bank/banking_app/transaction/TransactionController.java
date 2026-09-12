package com.bank.banking_app.transaction;

import com.bank.banking_app.transaction.dto.TransactionRequestDTO;
import com.bank.banking_app.transaction.dto.TransactionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService,
                                 TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@Valid @RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@Valid @RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.withdraw(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @RequestBody TransactionRequestDTO request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<Page<TransactionResponseDTO>> getHistory(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId, page, size));
    }

    @GetMapping("/statement/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getStatement(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getAccountStatement(accountId));
    }

    @GetMapping("/filter/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getByDateRange(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(accountId, start, end));
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<Map<String, Object>> getReceipt(@PathVariable Long id) {
        Transaction txn = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Map<String, Object> receipt = new LinkedHashMap<>();
        receipt.put("receiptNumber", "RCPT" + txn.getTransactionId() + System.currentTimeMillis() % 10000);
        receipt.put("referenceNumber", txn.getReferenceNumber());
        receipt.put("type", txn.getType());
        receipt.put("amount", "₹" + txn.getAmount());
        receipt.put("status", txn.getStatus());
        receipt.put("description", txn.getDescription());
        receipt.put("date", txn.getDate());
        receipt.put("bankName", "NeoVault Private Banking");
        receipt.put("message", "Transaction Successful");

        return ResponseEntity.ok(receipt);
    }
}
package com.bank.banking_app.fixeddeposit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/fd")
public class FixedDepositController {

    private final FixedDepositService fixedDepositService;

    public FixedDepositController(FixedDepositService fixedDepositService) {
        this.fixedDepositService = fixedDepositService;
    }

    @PostMapping
    public ResponseEntity<FixedDeposit> createFD(@RequestBody FixedDeposit fd) {
        return ResponseEntity.ok(fixedDepositService.createFD(fd));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<FixedDeposit>> getFDsByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(fixedDepositService.getFDsByAccount(accountId));
    }

    @GetMapping
    public ResponseEntity<List<FixedDeposit>> getAllFDs() {
        return ResponseEntity.ok(fixedDepositService.getAllFDs());
    }
}
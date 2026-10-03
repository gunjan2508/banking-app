package com.bank.banking_app.beneficiary;

import com.bank.banking_app.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;
    private final JwtUtil jwtUtil;
    private final com.bank.banking_app.security.UserRepository userRepository;

    public BeneficiaryController(BeneficiaryService beneficiaryService, JwtUtil jwtUtil,
                                 com.bank.banking_app.security.UserRepository userRepository) {
        this.beneficiaryService = beneficiaryService;
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

    @PostMapping
    public ResponseEntity<Beneficiary> addBeneficiary(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Beneficiary beneficiary) {
        return ResponseEntity.ok(beneficiaryService.addBeneficiary(getUserId(authHeader), beneficiary));
    }

    @GetMapping
    public ResponseEntity<List<Beneficiary>> getBeneficiaries(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiaries(getUserId(authHeader)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.ok("Beneficiary deleted");
    }
}
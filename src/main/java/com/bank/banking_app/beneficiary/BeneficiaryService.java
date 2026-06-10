package com.bank.banking_app.beneficiary;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public Beneficiary addBeneficiary(Long userId, Beneficiary beneficiary) {
        beneficiary.setUserId(userId);
        beneficiary.setAddedAt(LocalDateTime.now());
        return beneficiaryRepository.save(beneficiary);
    }

    public List<Beneficiary> getBeneficiaries(Long userId) {
        return beneficiaryRepository.findByUserId(userId);
    }

    public void deleteBeneficiary(Long id) {
        beneficiaryRepository.deleteById(id);
    }
}
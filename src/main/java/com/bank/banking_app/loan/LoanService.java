package com.bank.banking_app.loan;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    // Apply for loan
    public Loan applyLoan(Loan loan) {
        loan.setStatus("APPLIED");
        loan.setAppliedAt(LocalDateTime.now());
        loan.setEmi(calculateEmi(loan.getAmount(), loan.getTenure(), loan.getLoanType()));
        return loanRepository.save(loan);
    }

    // EMI Calculation formula
    // EMI = P * R * (1+R)^N / ((1+R)^N - 1)
    // P = principal, R = monthly rate (10% annual = 0.10/12), N = tenure months
    private BigDecimal calculateEmi(BigDecimal principal, int tenureMonths, String loanType) {
        double annualRate;
        if(loanType==null) annualRate=12.0;
        else switch (loanType.toUpperCase()) {
            case "HOME": annualRate = 8.5; break;
            case "CAR": annualRate = 9.5; break;
            case "EDUCATION": annualRate = 10.5; break;
            default: annualRate = 12.0; break;
        }
        double p = principal.doubleValue();
        double r = annualRate / 12 / 100;
        double n = tenureMonths;
        double emi = (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
        return BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP);
    }

    // Get loan by ID
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
    }

    // Get all loans by account
    public List<Loan> getLoansByAccount(Long accountId) {
        return loanRepository.findByAccountId(accountId);
    }

    // Approve loan
    public Loan approveLoan(Long id) {
        Loan loan = getLoanById(id);
        loan.setStatus("APPROVED");
        return loanRepository.save(loan);
    }

    // Reject loan
    public Loan rejectLoan(Long id) {
        Loan loan = getLoanById(id);
        loan.setStatus("REJECTED");
        return loanRepository.save(loan);
    }

    // Get all loans
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}
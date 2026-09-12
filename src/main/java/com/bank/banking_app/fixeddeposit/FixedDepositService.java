package com.bank.banking_app.fixeddeposit;

import com.bank.banking_app.account.Account;
import com.bank.banking_app.account.AccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FixedDepositService {

    private final FixedDepositRepository fixedDepositRepository;
    private final AccountRepository accountRepository;

    public FixedDepositService(FixedDepositRepository fixedDepositRepository,
                               AccountRepository accountRepository) {
        this.fixedDepositRepository = fixedDepositRepository;
        this.accountRepository = accountRepository;
    }

    public FixedDeposit createFD(FixedDeposit fd) {
        Account account = accountRepository.findById(fd.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(fd.getPrincipal()) < 0) {
            throw new RuntimeException("Insufficient balance to create Fixed Deposit");
        }

        // Deduct from account
        account.setBalance(account.getBalance().subtract(fd.getPrincipal()));
        accountRepository.save(account);

        // Calculate maturity amount: A = P(1 + r/n)^(nt)
        double p = fd.getPrincipal().doubleValue();
        double r = fd.getInterestRate().doubleValue() / 100;
        double t = fd.getTenureMonths() / 12.0;
        double maturity = p * Math.pow(1 + r, t);

        fd.setMaturityAmount(BigDecimal.valueOf(maturity).setScale(2, RoundingMode.HALF_UP));
        fd.setMaturityDate(LocalDate.now().plusMonths(fd.getTenureMonths()));
        fd.setStatus("ACTIVE");
        fd.setCreatedAt(LocalDateTime.now());

        return fixedDepositRepository.save(fd);
    }

    public List<FixedDeposit> getFDsByAccount(Long accountId) {
        return fixedDepositRepository.findByAccountId(accountId);
    }

    public List<FixedDeposit> getAllFDs() {
        return fixedDepositRepository.findAll();
    }
}
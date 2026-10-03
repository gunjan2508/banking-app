package com.bank.banking_app.scheduler;

import com.bank.banking_app.account.Account;
import com.bank.banking_app.account.AccountRepository;
import com.bank.banking_app.audit.AuditLogService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class InterestScheduler {

    private final AccountRepository accountRepository;
    private final AuditLogService auditLogService;

    public InterestScheduler(AccountRepository accountRepository, AuditLogService auditLogService) {
        this.accountRepository = accountRepository;
        this.auditLogService = auditLogService;
    }

    // Runs on 1st of every month at 12:00 AM
    // For testing change to: "*/60 * * * * *" (every 60 seconds)
    @Scheduled(cron = "0 0 0 1 * *")
    public void applyMonthlyInterest() {
        List<Account> savingsAccounts = accountRepository
                .findAll()
                .stream()
                .filter(a -> "SAVINGS".equals(a.getAccountType())
                        && "ACTIVE".equals(a.getStatus())
                        && a.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .toList();

        for (Account account : savingsAccounts) {
            // 4% annual = 0.33% monthly
            BigDecimal interest = account.getBalance()
                    .multiply(new BigDecimal("0.0033"))
                    .setScale(2, RoundingMode.HALF_UP);

            account.setBalance(account.getBalance().add(interest));
            accountRepository.save(account);

            auditLogService.log("SYSTEM", "INTEREST_CREDIT",
                    "Monthly interest ₹" + interest + " credited to account: " + account.getAccountNumber());
        }

        System.out.println("✅ Monthly interest applied to " + savingsAccounts.size() + " accounts");
    }
}
package com.bank.banking_app.account;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Create Account
    public Account createAccount(Account account) {
        account.setStatus("ACTIVE");
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    // Get Account by ID
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    // Get All Accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Check Balance
    public BigDecimal getBalance(Long id) {
        Account account = getAccountById(id);
        return account.getBalance();
    }

    // Update Account
    public Account updateAccount(Long id, Account updatedAccount) {
        Account existing = getAccountById(id);
        existing.setName(updatedAccount.getName());
        existing.setPhone(updatedAccount.getPhone());
        existing.setAccountType(updatedAccount.getAccountType());
        return accountRepository.save(existing);
    }

    // Close Account
    public String closeAccount(Long id) {
        Account account = getAccountById(id);
        account.setStatus("CLOSED");
        accountRepository.save(account);
        return "Account closed successfully";
    }
}
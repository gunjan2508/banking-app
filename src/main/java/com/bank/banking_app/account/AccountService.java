package com.bank.banking_app.account;

import com.bank.banking_app.account.dto.AccountRequestDTO;
import com.bank.banking_app.account.dto.AccountResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Convert Entity to DTO
    private AccountResponseDTO toDTO(Account account) {
        return new AccountResponseDTO(
                account.getAccountId(),
                account.getName(),
                account.getEmail(),
                account.getPhone(),
                account.getBalance(),
                account.getAccountType(),
                account.getStatus(),
                account.getCreatedAt()
        );
    }

    // Create Account
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        Account account = new Account();
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAccountType(request.getAccountType());
        account.setStatus("ACTIVE");
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        return toDTO(accountRepository.save(account));
    }

    // Get Account by ID
    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return toDTO(account);
    }

    // Get All Accounts with Pagination
    public Page<AccountResponseDTO> getAllAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findAll(pageable).map(this::toDTO);
    }

    // Check Balance
    public BigDecimal getBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return account.getBalance();
    }

    // Update Account
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setName(request.getName());
        account.setPhone(request.getPhone());
        account.setAccountType(request.getAccountType());
        return toDTO(accountRepository.save(account));
    }

    // Close Account
    public String closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setStatus("CLOSED");
        accountRepository.save(account);
        return "Account closed successfully";
    }
}
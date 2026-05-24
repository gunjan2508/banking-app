package com.bank.banking_app.account;

import com.bank.banking_app.account.dto.AccountRequestDTO;
import com.bank.banking_app.account.dto.AccountResponseDTO;
import com.bank.banking_app.audit.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuditLogService auditLogService;

    public AccountService(AccountRepository accountRepository, AuditLogService auditLogService) {
        this.accountRepository = accountRepository;
        this.auditLogService = auditLogService;
    }

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

    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        Account account = new Account();
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAccountType(request.getAccountType());
        account.setStatus("ACTIVE");
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        AccountResponseDTO response = toDTO(accountRepository.save(account));
        auditLogService.log("system", "CREATE_ACCOUNT", "Account created for: " + request.getEmail());
        return response;
    }

    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return toDTO(account);
    }

    public Page<AccountResponseDTO> getAllAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountRepository.findAll(pageable).map(this::toDTO);
    }

    public BigDecimal getBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return account.getBalance();
    }

    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setName(request.getName());
        account.setPhone(request.getPhone());
        account.setAccountType(request.getAccountType());
        AccountResponseDTO response = toDTO(accountRepository.save(account));
        auditLogService.log("system", "UPDATE_ACCOUNT", "Account updated: " + id);
        return response;
    }

    public String closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setStatus("CLOSED");
        accountRepository.save(account);
        auditLogService.log("system", "CLOSE_ACCOUNT", "Account closed: " + id);
        return "Account closed successfully";
    }
}
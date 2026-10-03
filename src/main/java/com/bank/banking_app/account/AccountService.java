package com.bank.banking_app.account;

import com.bank.banking_app.account.dto.AccountRequestDTO;
import com.bank.banking_app.account.dto.AccountResponseDTO;
import com.bank.banking_app.audit.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId(account.getAccountId());
        dto.setName(account.getName());
        dto.setEmail(account.getEmail());
        dto.setPhone(account.getPhone());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getAccountType());
        dto.setStatus(account.getStatus());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setAccountNumber(account.getAccountNumber());
        return dto;
    }

    private String generateAccountNumber() {
        int year = LocalDateTime.now().getYear();
        long count = accountRepository.count() + 1;
        return String.format("NV%d%06d", year, count);
    }

    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        Account account = new Account();
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        account.setAccountNumber(generateAccountNumber());
        auditLogService.log("system", "CREATE_ACCOUNT", "Account created for: " + request.getName());
        return toDTO(accountRepository.save(account));
    }

    public Page<AccountResponseDTO> getAllAccounts(int page, int size) {
        return accountRepository.findAll(PageRequest.of(page, size)).map(this::toDTO);
    }

    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return toDTO(account);
    }

    public BigDecimal getBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance();
    }

    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAccountType(request.getAccountType());
        auditLogService.log("system", "UPDATE_ACCOUNT", "Account updated: " + id);
        return toDTO(accountRepository.save(account));
    }

    public String closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("CLOSED");
        auditLogService.log("system", "CLOSE_ACCOUNT", "Account closed: " + id);
        accountRepository.save(account);
        return "Account closed successfully";
    }
}
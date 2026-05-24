package com.bank.banking_app.transaction;

import com.bank.banking_app.account.Account;
import com.bank.banking_app.account.AccountRepository;
import com.bank.banking_app.audit.AuditLogService;
import com.bank.banking_app.transaction.dto.TransactionRequestDTO;
import com.bank.banking_app.transaction.dto.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AuditLogService auditLogService;

    // Daily transfer limit
    private static final BigDecimal DAILY_LIMIT = new BigDecimal("100000");

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              AuditLogService auditLogService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.auditLogService = auditLogService;
    }

    private TransactionResponseDTO toDTO(Transaction t) {
        return new TransactionResponseDTO(
                t.getTransactionId(), t.getFromAccountId(), t.getToAccountId(),
                t.getAmount(), t.getType(), t.getStatus(),
                t.getDescription(), t.getReferenceNumber(), t.getDate()
        );
    }

    private String generateReferenceNumber() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // DEPOSIT
    @Transactional
    public TransactionResponseDTO deposit(TransactionRequestDTO request) {
        Account account = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getStatus().equals("CLOSED")) {
            throw new RuntimeException("Cannot deposit to closed account");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setFromAccountId(request.getFromAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setType("DEPOSIT");
        transaction.setStatus("SUCCESS");
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Deposit");
        transaction.setReferenceNumber(generateReferenceNumber());
        transaction.setDate(LocalDateTime.now());

        auditLogService.log("system", "DEPOSIT", "Deposited " + request.getAmount() + " to account: " + request.getFromAccountId());
        return toDTO(transactionRepository.save(transaction));
    }

    // WITHDRAW
    @Transactional
    public TransactionResponseDTO withdraw(TransactionRequestDTO request) {
        Account account = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getStatus().equals("CLOSED")) {
            throw new RuntimeException("Cannot withdraw from closed account");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setFromAccountId(request.getFromAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setType("WITHDRAW");
        transaction.setStatus("SUCCESS");
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Withdrawal");
        transaction.setReferenceNumber(generateReferenceNumber());
        transaction.setDate(LocalDateTime.now());

        auditLogService.log("system", "WITHDRAW", "Withdrawn " + request.getAmount() + " from account: " + request.getFromAccountId());
        return toDTO(transactionRepository.save(transaction));
    }

    // TRANSFER with daily limit check
    @Transactional
    public TransactionResponseDTO transfer(TransactionRequestDTO request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        Account sender = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account receiver = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (sender.getStatus().equals("CLOSED")) {
            throw new RuntimeException("Sender account is closed");
        }

        if (receiver.getStatus().equals("CLOSED")) {
            throw new RuntimeException("Receiver account is closed");
        }

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Daily limit check
        BigDecimal spentToday = transactionRepository.getTotalSpentToday(
                request.getFromAccountId(),
                LocalDateTime.now().toLocalDate().atStartOfDay()
        );

        if (spentToday.add(request.getAmount()).compareTo(DAILY_LIMIT) > 0) {
            throw new RuntimeException("Daily transfer limit of ₹1,00,000 exceeded");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));
        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setFromAccountId(request.getFromAccountId());
        transaction.setToAccountId(request.getToAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setType("TRANSFER");
        transaction.setStatus("SUCCESS");
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Transfer");
        transaction.setReferenceNumber(generateReferenceNumber());
        transaction.setDate(LocalDateTime.now());

        auditLogService.log("system", "TRANSFER", "Transferred " + request.getAmount() +
                " from account " + request.getFromAccountId() + " to " + request.getToAccountId());
        return toDTO(transactionRepository.save(transaction));
    }

    // GET TRANSACTION HISTORY with pagination
    public Page<TransactionResponseDTO> getTransactionHistory(Long accountId, int page, int size) {
        return transactionRepository.findByAccountId(accountId, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    // GET ACCOUNT STATEMENT - last 10 transactions
    public List<TransactionResponseDTO> getAccountStatement(Long accountId) {
        return transactionRepository.findTop10ByAccountId(accountId, PageRequest.of(0, 10))
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // GET TRANSACTIONS BY DATE RANGE
    public List<TransactionResponseDTO> getTransactionsByDateRange(
            Long accountId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByAccountIdAndDateRange(accountId, start, end)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}
package com.bank.banking_app.transaction;

import com.bank.banking_app.account.Account;
import com.bank.banking_app.account.AccountRepository;
import com.bank.banking_app.audit.AuditLogService;
import com.bank.banking_app.notification.NotificationService;
import com.bank.banking_app.security.UserRepository;
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
    private final com.bank.banking_app.notification.NotificationService notificationService;

    private static final BigDecimal DAILY_LIMIT = new BigDecimal("100000");

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              AuditLogService auditLogService,
                              com.bank.banking_app.notification.NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.auditLogService = auditLogService;
        this.notificationService = notificationService;
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

    @Transactional
    public TransactionResponseDTO deposit(TransactionRequestDTO request) {
        Account account = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getStatus().equals("CLOSED"))
            throw new RuntimeException("Cannot deposit to closed account");
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
       // notificationService.createNotification(1L, "Deposit Successful", "₹" + request.getAmount() + " credited to your account", "DEPOSIT");
        return toDTO(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponseDTO withdraw(TransactionRequestDTO request) {
        Account account = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getStatus().equals("CLOSED"))
            throw new RuntimeException("Cannot withdraw from closed account");
        if (account.getBalance().compareTo(request.getAmount()) < 0)
            throw new RuntimeException("Insufficient balance");
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
        //notificationService.createNotification(1L, "Withdrawal Successful", "₹" + request.getAmount() + " withdrawn from your account", "WITHDRAW");
        return toDTO(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponseDTO transfer(TransactionRequestDTO request) {
        if (request.getFromAccountId().equals(request.getToAccountId()))
            throw new RuntimeException("Cannot transfer to same account");
        Account sender = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        Account receiver = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));
        if (sender.getStatus().equals("CLOSED")) throw new RuntimeException("Sender account is closed");
        if (receiver.getStatus().equals("CLOSED")) throw new RuntimeException("Receiver account is closed");
        if (sender.getBalance().compareTo(request.getAmount()) < 0) throw new RuntimeException("Insufficient balance");
        BigDecimal spentToday = transactionRepository.getTotalSpentToday(request.getFromAccountId(), LocalDateTime.now().toLocalDate().atStartOfDay());
        if (spentToday.add(request.getAmount()).compareTo(DAILY_LIMIT) > 0)
            throw new RuntimeException("Daily transfer limit of ₹1,00,000 exceeded");
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
        auditLogService.log("system", "TRANSFER", "Transferred " + request.getAmount() + " from " + request.getFromAccountId() + " to " + request.getToAccountId());
        // notificationService.createNotification(senderUserId, "₹" + request.getAmount() + " transferred. Ref: " + transaction.getReferenceNumber(), "TRANSFER");
        return toDTO(transactionRepository.save(transaction));
    }

    public Page<TransactionResponseDTO> getTransactionHistory(Long accountId, int page, int size) {
        return transactionRepository.findByAccountId(accountId, PageRequest.of(page, size)).map(this::toDTO);
    }

    public List<TransactionResponseDTO> getAccountStatement(Long accountId) {
        return transactionRepository.findTop10ByAccountId(accountId, PageRequest.of(0, 10))
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsByDateRange(Long accountId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByAccountIdAndDateRange(accountId, start, end)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}
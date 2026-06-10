package com.bank.banking_app.beneficiary;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String name;
    private Long accountId;
    private String nickname;
    private String bankName;
    private LocalDateTime addedAt;

    public Beneficiary() {}

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public Long getAccountId() { return accountId; }
    public String getNickname() { return nickname; }
    public String getBankName() { return bankName; }
    public LocalDateTime getAddedAt() { return addedAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
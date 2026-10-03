package com.bank.banking_app.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByAccountId(Long accountId);
}
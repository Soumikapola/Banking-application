package com.example.Banking_services.service;

import com.example.Banking_services.model.Account;
import com.example.Banking_services.model.Transaction;
import com.example.Banking_services.repository.AccountRepository;
import com.example.Banking_services.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankingService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional // <--- CRITICAL: Do all of this together or none of it!
    public void transfer(Account fromAccount, String toAccountNumber, BigDecimal amount) {
        // 1. Find the target account
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 2. Validate sufficient funds
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // 3. Perform the deduction and addition
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        // 4. Save the changes to accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // 5. Record the transaction
        Transaction transaction = new Transaction(amount, fromAccount, toAccount, "TRANSFER");
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(Account account) {
        // Find all transfers involving this account (sent OR received)
        return transactionRepository
                .findBySourceAccountIdOrTargetAccountIdOrderByTimestampDesc(account.getId(), account.getId());
    }

    @Transactional
    public void deposit(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Source is NULL for deposit
        Transaction transaction = new Transaction(amount, null, account, "DEPOSIT");
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Target is NULL for withdrawal
        Transaction transaction = new Transaction(amount, account, null, "WITHDRAW");
        transactionRepository.save(transaction);
    }
}

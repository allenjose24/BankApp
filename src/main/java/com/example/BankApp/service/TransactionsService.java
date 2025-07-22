package com.example.BankApp.service;

import com.example.BankApp.model.*;
import com.example.BankApp.repository.AccountRepository;
import com.example.BankApp.repository.TransactionsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionsService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private AccountRepository accountRepository;

    // 💰 Deposit money into an account
    @Transactional
    public Transactions deposit(Long accountId, BigDecimal amount) {
        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transactions txn = new Transactions();
        txn.setToAccount(account); // 📥 deposit into this account
        txn.setAmount(amount);
        txn.setTransactionType(TransactionType.DEPOSIT);
        txn.setTransactionTime(LocalDateTime.now());

        return transactionsRepository.save(txn);
    }

    // 🏧 Withdraw money from an account
    @Transactional
    public Transactions withdraw(Long accountId, BigDecimal amount) {
        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transactions txn = new Transactions();
        txn.setFromAccount(account); // 💸 withdraw from this account
        txn.setAmount(amount);
        txn.setTransactionType(TransactionType.WITHDRAW);
        txn.setTransactionTime(LocalDateTime.now());

        return transactionsRepository.save(txn);
    }

    // 🔁 Transfer money from one account to another
    @Transactional
    public Transactions transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Accounts fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Accounts toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds for transfer");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transactions txn = new Transactions();
        txn.setFromAccount(fromAccount);
        txn.setToAccount(toAccount);
        txn.setAmount(amount);
        txn.setTransactionType(TransactionType.TRANSFER);
        txn.setTransactionTime(LocalDateTime.now());

        return transactionsRepository.save(txn);
    }

}

package com.example.BankApp.service;

import com.example.BankApp.model.*;
import com.example.BankApp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountsService {

    @Autowired
    private AccountRepository accountsRepository;

    public Accounts createAccountForUser(Users user, String type, BigDecimal balance) {
        // Step 1: Create and save account to get the ID
        Accounts account = new Accounts();
        account.setAccountType(AccountType.valueOf(type));
        account.setBalance(balance);
        account.setUser(user);

        account = accountsRepository.save(account); // now it has an ID

        // Step 2: Generate account number using prefix, first letter of name, and ID
        String accountNumber = "ACC_" + user.getName().toUpperCase().charAt(0) + "_" + account.getId();
        account.setAccountNumber(accountNumber);

        return accountsRepository.save(account); // update accountNumber
    }

    public List<Accounts> getAccountsByUser(Users user) {
        return accountsRepository.findByUser(user);
    }

    public Accounts updateAccountStatus(Long accountId, AccountStatus status) {
        Accounts acc = accountsRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        acc.setStatus(status);
        return accountsRepository.save(acc);
    }

    public void deleteAccount(Long accountId, Users requestingUser) {
        Accounts acc = accountsRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        // Optional: validate user/role permissions here
        accountsRepository.delete(acc);
    }

    public Accounts deposit(Long accountId, BigDecimal amount) {
        Accounts acc = accountsRepository.findById(accountId).orElseThrow();
        acc.setBalance(acc.getBalance().add(amount));
        return accountsRepository.save(acc);
    }

    public Accounts withdraw(Long accountId, BigDecimal amount) {
        Accounts acc = accountsRepository.findById(accountId).orElseThrow();
        if (acc.getBalance().compareTo(amount) < 0) throw new RuntimeException("Insufficient funds");
        acc.setBalance(acc.getBalance().subtract(amount));
        return accountsRepository.save(acc);
    }


    public boolean isAccountOwnedByUser(Long accountId, Long userId) {
        return accountsRepository.existsByIdAndUserId(accountId, userId);
    }

    public BigDecimal getBalance(Long accountId) {
        return accountsRepository.findById(accountId).orElseThrow().getBalance();
    }


    public Accounts getAccountIfAuthorized(Long id, Users user) {
        Accounts account = accountsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (user.getRole()!=Role.MANAGER && !account.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Access denied: Not your account.");
        }

        return account;
    }

    public Accounts updateAccountStatusAsManager(Long id, AccountStatus status, Users manager) {
        if (!manager.getRole().equals(Role.MANAGER)) {
            throw new SecurityException("Only managers can update account status.");
        }

        Accounts account = accountsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setStatus(status);
        return accountsRepository.save(account);
    }
}

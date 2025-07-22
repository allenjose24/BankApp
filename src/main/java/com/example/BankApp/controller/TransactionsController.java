package com.example.BankApp.controller;

import com.example.BankApp.model.Transactions;
import com.example.BankApp.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;

    // 💰 Deposit endpoint
    @PostMapping("/deposit")
    public Transactions deposit(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount
    ) {
        return transactionsService.deposit(accountId, amount);
    }

    // 🏧 Withdraw endpoint
    @PostMapping("/withdraw")
    public Transactions withdraw(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount
    ) {
        return transactionsService.withdraw(accountId, amount);
    }

    // 🔁 Transfer endpoint
    @PostMapping("/transfer")
    public Transactions transfer(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount
    ) {
        return transactionsService.transfer(fromAccountId, toAccountId, amount);
    }
}

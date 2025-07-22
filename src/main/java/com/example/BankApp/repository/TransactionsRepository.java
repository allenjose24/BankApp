package com.example.BankApp.repository;

import com.example.BankApp.model.Transactions;
import com.example.BankApp.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    // 🔍 Find all transactions related to a specific account (from or to)
    List<Transactions> findByFromAccountOrToAccount(Accounts from, Accounts to);

    // 🔍 Optional: find all sent transactions
    List<Transactions> findByFromAccount(Accounts from);

    // 🔍 Optional: find all received transactions
    List<Transactions> findByToAccount(Accounts to);

    List<Transactions> findByFromAccountId(Long accountId);
    List<Transactions> findByToAccountId(Long accountId);
    List<Transactions> findByFromAccountIdOrToAccountId(Long fromId, Long toId);
}

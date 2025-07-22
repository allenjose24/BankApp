package com.example.BankApp.repository;

import com.example.BankApp.model.Accounts;
import com.example.BankApp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Accounts, Long> {
    List<Accounts> findByUserId(Long userId);

    List<Accounts> findByUser(Users user);

    boolean existsByIdAndUserId(Long id, Long userId);
}

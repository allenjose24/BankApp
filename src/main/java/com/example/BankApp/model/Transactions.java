package com.example.BankApp.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Type: DEPOSIT, WITHDRAW, TRANSFER
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private BigDecimal amount;

    private LocalDateTime transactionTime;

    private String description;

    // Source Account (e.g., sender in transfer)
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Accounts fromAccount;

    // Target Account (e.g., receiver in transfer)
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Accounts toAccount;
}


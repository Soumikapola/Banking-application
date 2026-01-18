package com.example.Banking_services.model;

import jakarta.persistence.*;
import lombok.Data; // Reduces boilerplate code
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private LocalDateTime timestamp;

    private String type; // DEPOSIT, WITHDRAW, TRANSFER

    @ManyToOne // Many transactions can belong to ONE account
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    // Default constructor is required by JPA
    public Transaction() {}

    public Transaction(BigDecimal amount, Account sourceAccount, Account targetAccount, String type) {
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.timestamp = LocalDateTime.now();
        this.type = type;
    }
}
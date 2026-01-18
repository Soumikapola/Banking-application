package com.example.Banking_services.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal; // Use BigDecimal for money, NEVER Double!
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    // BigDecimal is better for money because it has exact precision.
    // Double can have rounding errors (e.g. 1.000000001).
    @Column(nullable = false)
    private BigDecimal balance;

    @OneToOne // Relationship: One User has One Account
    @JoinColumn(name = "user_id") // Creates a foreign key column in 'accounts' table
    private User user;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "targetAccount", cascade = CascadeType.ALL)
    private List<Transaction> receivedTransactions;
}
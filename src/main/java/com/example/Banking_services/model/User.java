package com.example.Banking_services.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    private String fullName;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Account account;
}

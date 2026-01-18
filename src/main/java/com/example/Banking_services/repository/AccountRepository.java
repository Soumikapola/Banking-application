package com.example.Banking_services.repository;

import com.example.Banking_services.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
// Later we might need to find by account number
    Optional<Account> findByAccountNumber(String accountNumber);
}

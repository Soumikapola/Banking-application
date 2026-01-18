package com.example.Banking_services.repository;


import com.example.Banking_services.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Magic Method: Spring Data automagically implements this Query!
    // "Find all transactions where source account is ID OR target account is ID, ordered by date"
    List<Transaction> findBySourceAccountIdOrTargetAccountIdOrderByTimestampDesc(Long sourceAccountId, Long targetAccountId);
}
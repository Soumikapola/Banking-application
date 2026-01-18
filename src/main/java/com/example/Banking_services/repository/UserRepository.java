package com.example.Banking_services.repository;

import com.example.Banking_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


// JpaRepository<Entity, ID Type>
public interface UserRepository extends JpaRepository<User, Long> {
    // Magic Method!
    // Spring sees "findByUsername" and auto-generates the SQL:
    // "SELECT * FROM users WHERE username = ?"
    Optional<User> findByUsername(String username);
}

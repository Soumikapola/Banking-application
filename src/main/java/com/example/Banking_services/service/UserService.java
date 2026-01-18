package com.example.Banking_services.service;

import com.example.Banking_services.model.Account;
import com.example.Banking_services.model.User;
import com.example.Banking_services.repository.AccountRepository;
import com.example.Banking_services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        String password = user.getPassword();
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*\\d.*")) {
            throw new RuntimeException("Password must contain at least one number");
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            throw new RuntimeException("Password must contain at least one special character");
        }

        if (user.getUsername().length() < 4) {
            throw new RuntimeException("Username must be at least 4 characters long");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(0.0));
        account.setUser(savedUser);

        account.setAccountNumber("ACC" + System.currentTimeMillis());

        accountRepository.save(account);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            existingUser.setFullName(user.getFullName());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(existingUser);
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(userId);
    }
}

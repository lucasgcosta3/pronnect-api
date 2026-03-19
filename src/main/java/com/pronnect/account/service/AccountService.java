package com.pronnect.account.service;

import com.pronnect.account.repository.AccountRepository;
import com.pronnect.account.entity.AccountRole;
import com.pronnect.account.entity.Account;
import com.pronnect.exception.EmailAlreadyExistsException;
import com.pronnect.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Account register(String name, String email, String password, AccountRole role) {

        email = email.toLowerCase();
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Account account = buildAccount(name, email, password, role);

        return repository.save(account);
    }

    public Account findByEmail(String email) {
        return repository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    private Account buildAccount(String name, String email, String password, AccountRole role) {

        return Account.builder()
                .id(UUID.randomUUID())
                .name(name)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .build();
    }
}

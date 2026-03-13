package com.pronnect.account;

import com.pronnect.exception.EmailAlreadyExistsException;
import com.pronnect.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Account register(String name, String email, String password, AccountRole role) {

        email = email.toLowerCase();
        if(repository.existsByEmail(email)) {
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
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();
    }
}

package com.pronnect.auth.service;

import com.pronnect.account.dto.AccountResponse;
import com.pronnect.account.dto.RegisterAccountRequest;
import com.pronnect.account.entity.Account;
import com.pronnect.account.repository.AccountRepository;
import com.pronnect.auth.dto.AuthResponse;
import com.pronnect.auth.dto.LoginRequest;
import com.pronnect.exception.EmailAlreadyExistsException;
import com.pronnect.exception.InvalidCredentialsException;
import com.pronnect.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AccountResponse register(RegisterAccountRequest request) {

        if (accountRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Account account = new Account();

        account.setName(request.name());
        account.setEmail(request.email());
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        account.setRole(request.role());

        accountRepository.save(account);

        return new AccountResponse(account.getId(), account.getEmail());
    }

    public AuthResponse login(LoginRequest request) {

        Account account = accountRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if (!passwordEncoder.matches(request.password(), account.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(account);

        return new AuthResponse(token);
    }

}
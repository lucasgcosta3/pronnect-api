package com.pronnect.auth.service;

import com.pronnect.account.entity.Account;
import com.pronnect.account.repository.AccountRepository;
import com.pronnect.auth.dto.AuthResponse;
import com.pronnect.auth.dto.LoginRequest;
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
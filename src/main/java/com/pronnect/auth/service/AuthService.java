package com.pronnect.auth.service;

import com.pronnect.account.dto.AccountResponse;
import com.pronnect.account.dto.RegisterAccountRequest;
import com.pronnect.account.entity.Account;
import com.pronnect.account.repository.AccountRepository;
import com.pronnect.account.enums.AccountRole;
import com.pronnect.account.validation.CpfCnpjValidator;
import com.pronnect.auth.dto.AuthResponse;
import com.pronnect.auth.dto.LoginRequest;
import com.pronnect.exception.BusinessException;
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

        if (request.role() == AccountRole.PROFESSIONAL) {
            if (!CpfCnpjValidator.isValidCpf(request.cpf())) {
                throw new BusinessException("Invalid CPF");
            }
            if (accountRepository.existsByCpf(request.cpf())) {
                throw new BusinessException("CPF already in use");
            }
        } else if (request.role() == AccountRole.COMPANY) {
            if ("PF".equalsIgnoreCase(request.personType())) {
                if (!CpfCnpjValidator.isValidCpf(request.cpf())) {
                    throw new BusinessException("Invalid CPF");
                }
                if (accountRepository.existsByCpf(request.cpf())) {
                    throw new BusinessException("CPF already in use");
                }
            } else if ("PJ".equalsIgnoreCase(request.personType())) {
                if (!CpfCnpjValidator.isValidCnpj(request.cnpj())) {
                    throw new BusinessException("Invalid CNPJ");
                }
                if (accountRepository.existsByCnpj(request.cnpj())) {
                    throw new BusinessException("CNPJ already in use");
                }
            } else {
                throw new BusinessException("personType must be PF or PJ for COMPANY");
            }
        }

        Account account = new Account();

        account.setName(request.name());
        account.setEmail(request.email());
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        account.setRole(request.role());
        account.setCpf(request.cpf());
        account.setCnpj(request.cnpj());
        account.setPersonType(request.personType());

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
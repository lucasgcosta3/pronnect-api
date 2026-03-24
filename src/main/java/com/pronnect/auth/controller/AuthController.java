package com.pronnect.auth.controller;

import com.pronnect.account.dto.AccountResponse;
import com.pronnect.account.dto.RegisterAccountRequest;
import com.pronnect.auth.dto.AuthResponse;
import com.pronnect.auth.dto.LoginRequest;
import com.pronnect.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@RequestBody @Valid RegisterAccountRequest request) {
        AccountResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
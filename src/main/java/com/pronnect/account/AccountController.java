package com.pronnect.account;

import com.pronnect.account.dto.AccountResponse;
import com.pronnect.account.dto.RegisterAccountRequest;
import com.pronnect.account.mapper.AccountMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;
    private final AccountMapper mapper;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(
            @Valid @RequestBody RegisterAccountRequest request
    ) {

        Account account = service.register(
                request.name(),
                request.email(),
                request.password(),
                request.role()
        );

        AccountResponse response = mapper.toResponse(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

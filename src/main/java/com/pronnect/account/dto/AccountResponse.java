package com.pronnect.account.dto;

import com.pronnect.account.entity.AccountRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(

        UUID id,
        String name,
        String email,
        AccountRole role,
        LocalDateTime createdAt

) {}

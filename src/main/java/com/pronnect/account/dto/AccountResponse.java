package com.pronnect.account.dto;

import com.pronnect.account.enums.AccountRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String email
) {}

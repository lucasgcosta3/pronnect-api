package com.pronnect.servicecontract.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceContractResponse(
        UUID id,
        UUID proposalId,
        String status,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        LocalDateTime validatedAt
) {}
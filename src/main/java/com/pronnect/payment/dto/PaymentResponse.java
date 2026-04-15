package com.pronnect.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID serviceContractId,
        BigDecimal amount,
        BigDecimal platformFee,
        BigDecimal professionalAmount,
        String status,
        LocalDateTime createdAt,
        LocalDateTime releasedAt
) {}
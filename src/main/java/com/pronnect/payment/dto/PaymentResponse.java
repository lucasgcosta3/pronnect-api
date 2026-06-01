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
        String paymentMethod,
        String pixQrCode,
        String pixCopyPaste,
        LocalDateTime expiresAt,
        LocalDateTime createdAt,
        LocalDateTime releasedAt,
        LocalDateTime refundedAt
) {}
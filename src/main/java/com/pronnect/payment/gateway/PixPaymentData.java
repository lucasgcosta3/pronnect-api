package com.pronnect.payment.gateway;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PixPaymentData(
    String externalPaymentId,
    String pixQrCode,
    String pixCopyPaste,
    LocalDateTime expiresAt
) {}

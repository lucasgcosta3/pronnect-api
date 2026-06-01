package com.pronnect.payment.gateway;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MockPixPaymentGateway implements PaymentGateway {

    @Override
    public PixPaymentData createPixPayment(BigDecimal amount, String description, String payerEmail) {
        String externalPaymentId = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);

        String pixCopyPaste = buildPixEmvString(externalPaymentId, amount);

        return new PixPaymentData(
                externalPaymentId,
                pixCopyPaste,
                pixCopyPaste,
                expiresAt
        );
    }

    /**
     * Generates a plausible-looking PIX EMV string.
     * This is a simplified simulation — not a real EMV/TLV payload.
     */
    private String buildPixEmvString(String paymentId, BigDecimal amount) {
        String key = paymentId.replace("-", "").substring(0, 32);
        String txid = paymentId.replace("-", "").substring(0, 25).toUpperCase();
        String amountStr = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();

        String payload = String.format(
                "00020126580014br.gov.bcb.pix0136%s5204000053039865402%s5802BR5911PronnectPay6008Sao Paulo62070503%s6304",
                key,
                amountStr,
                txid.substring(0, 3)
        );

        // Append a mock CRC16 checksum (simplified)
        String crc = Integer.toHexString(crc16(payload + "6304")).toUpperCase();
        while (crc.length() < 4) crc = "0" + crc;

        return payload + crc;
    }

    /** CRC-16/CCITT-FALSE algorithm used by PIX EMV */
    private int crc16(String str) {
        int crc = 0xFFFF;
        byte[] bytes = str.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
                crc &= 0xFFFF;
            }
        }
        return crc;
    }

    @Override
    public String getPaymentStatus(String externalPaymentId) {
        return "PENDING";
    }

    @Override
    public boolean refundPayment(String externalPaymentId) {
        return true;
    }
}
package com.pronnect.payment.gateway;

import java.math.BigDecimal;

public interface PaymentGateway {
    
    PixPaymentData createPixPayment(BigDecimal amount, String description, String payerEmail);
    
    String getPaymentStatus(String externalPaymentId);
    
    boolean refundPayment(String externalPaymentId);
}

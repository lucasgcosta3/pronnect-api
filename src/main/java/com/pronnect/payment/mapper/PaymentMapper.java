package com.pronnect.payment.mapper;

import com.pronnect.payment.dto.PaymentResponse;
import com.pronnect.payment.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getServiceContract().getId(),
                payment.getAmount(),
                payment.getPlatformFee(),
                payment.getProfessionalAmount(),
                payment.getStatus().name(),
                payment.getCreatedAt(),
                payment.getReleasedAt()
        );
    }
}
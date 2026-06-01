package com.pronnect.payment.service;

import com.pronnect.payment.entity.Payment;
import com.pronnect.payment.enums.PaymentStatus;
import com.pronnect.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentSimulationScheduler {

    private final PaymentRepository paymentRepository;

    @Value("${payment.mock.auto-approve-seconds:10}")
    private int autoApproveSeconds;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void autoApprovePayments() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusSeconds(autoApproveSeconds);

        List<Payment> pendingPayments = paymentRepository.findByStatusAndCreatedAtBefore(
                PaymentStatus.PENDING,
                cutoffTime
        );

        for (Payment payment : pendingPayments) {
            payment.setStatus(PaymentStatus.HELD);
        }
    }
}
package com.pronnect.payment.repository;

import com.pronnect.payment.entity.Payment;
import com.pronnect.payment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByServiceContractId(UUID serviceContractId);

    List<Payment> findByStatusAndCreatedAtBefore(
            PaymentStatus status,
            LocalDateTime createdAt
    );
}
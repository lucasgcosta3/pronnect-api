package com.pronnect.payment.entity;

import com.pronnect.payment.enums.PaymentStatus;
import com.pronnect.servicecontract.entity.ServiceContract;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @OneToOne
    @JoinColumn(name = "service_contract_id")
    private ServiceContract serviceContract;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "platform_fee")
    private BigDecimal platformFee;

    @Column(name = "professional_amount")
    private BigDecimal professionalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
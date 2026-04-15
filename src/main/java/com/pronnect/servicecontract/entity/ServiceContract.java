package com.pronnect.servicecontract.entity;

import com.pronnect.proposal.entity.Proposal;
import com.pronnect.servicecontract.enums.ServiceContractStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_contract")
@Getter
@Setter
public class ServiceContract {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @OneToOne
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @Enumerated(EnumType.STRING)
    private ServiceContractStatus status;

    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @PrePersist
    public void prePersist() {
        this.startedAt = LocalDateTime.now();
    }
}
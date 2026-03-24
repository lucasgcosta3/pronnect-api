package com.pronnect.proposal.entity;

import com.pronnect.company.entity.CompanyProfile;
import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.proposal.enums.ProposalStatus;
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
public class Proposal {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyProfile company;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    private ProfessionalProfile professional;

    private String message;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

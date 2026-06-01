package com.pronnect.project.entity;

import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.project.enums.BidStatus;
import com.pronnect.proposal.entity.Proposal;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "project_bid", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "professional_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectBid {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private ProfessionalProfile professional;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "delivery_days", nullable = false)
    private int deliveryDays;

    @Column(name = "cover_letter", nullable = false, columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "portfolio_url", columnDefinition = "TEXT")
    private String portfolioUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BidStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

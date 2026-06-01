package com.pronnect.review.entity;

import com.pronnect.account.entity.Account;
import com.pronnect.servicecontract.entity.ServiceContract;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "review", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"reviewer_account_id", "reviewed_account_id", "service_contract_id"})
})
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "reviewer_account_id", nullable = false)
    private Account reviewerAccount;

    @ManyToOne
    @JoinColumn(name = "reviewed_account_id", nullable = false)
    private Account reviewedAccount;

    @ManyToOne
    @JoinColumn(name = "service_contract_id", nullable = false)
    private ServiceContract serviceContract;

    @Column(nullable = false)
    private Short rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

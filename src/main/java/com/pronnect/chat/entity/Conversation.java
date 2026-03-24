package com.pronnect.chat.entity;

import com.pronnect.proposal.entity.Proposal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Conversation {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @OneToOne
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

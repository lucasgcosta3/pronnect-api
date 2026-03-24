package com.pronnect.chat.repository;

import com.pronnect.chat.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findByProposalId(UUID proposalId);

    @Query("""
            SELECT c FROM Conversation c
            WHERE c.proposal.company.account.id = :accountId
               OR c.proposal.professional.account.id = :accountId
            ORDER BY c.createdAt DESC
            """)
    List<Conversation> findAllByAccountId(@Param("accountId") UUID accountId);
}

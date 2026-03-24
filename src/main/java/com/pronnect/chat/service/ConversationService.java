package com.pronnect.chat.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.chat.entity.Conversation;
import com.pronnect.chat.repository.ConversationRepository;
import com.pronnect.exception.BusinessException;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.exception.NotFoundException;
import com.pronnect.proposal.entity.Proposal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final AuthenticatedUserService auth;

    @Transactional
    public Conversation createForProposal(Proposal proposal) {

        boolean alreadyExists = conversationRepository
                .findByProposalId(proposal.getId())
                .isPresent();

        if (alreadyExists) {
            throw new BusinessException("A conversation already exists for this proposal");
        }

        Conversation conversation = new Conversation();
        conversation.setProposal(proposal);

        return conversationRepository.save(conversation);
    }

    @Transactional(readOnly = true)
    public List<Conversation> getMyConversations() {
        Account account = auth.getCurrentAccount();
        return conversationRepository.findAllByAccountId(account.getId());
    }

    @Transactional(readOnly = true)
    public Conversation getByProposalId(UUID proposalId) {
        Account account = auth.getCurrentAccount();

        Conversation conversation = conversationRepository.findByProposalId(proposalId)
                .orElseThrow(() -> new NotFoundException("Conversation not found"));

        assertIsParticipant(conversation, account.getId());

        return conversation;
    }

    @Transactional(readOnly = true)
    public Conversation getById(UUID id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation not found"));
    }

    private void assertIsParticipant(Conversation conversation, UUID accountId) {
        boolean isParticipant =
                conversation.getProposal().getCompany().getAccount().getId().equals(accountId)
                        || conversation.getProposal().getProfessional().getAccount().getId().equals(accountId);

        if (!isParticipant) {
            throw new ForbiddenException("You are not part of this conversation");
        }
    }
}

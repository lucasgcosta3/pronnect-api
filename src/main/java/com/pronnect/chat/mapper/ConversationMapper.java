package com.pronnect.chat.mapper;

import com.pronnect.account.entity.Account;
import com.pronnect.chat.dto.ConversationResponse;
import com.pronnect.chat.entity.Conversation;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConversationMapper {

    public ConversationResponse toResponse(Conversation conversation, UUID currentAccountId) {

        String otherPartyName = resolveOtherPartyName(conversation, currentAccountId);

        return new ConversationResponse(
                conversation.getId(),
                conversation.getProposal().getId(),
                otherPartyName,
                conversation.getProposal().getPrice(),
                conversation.getCreatedAt()
        );
    }

    private String resolveOtherPartyName(Conversation conversation, UUID currentAccountId) {

        UUID companyAccountId = conversation.getProposal().getCompany().getAccount().getId();

        if (companyAccountId.equals(currentAccountId)) {
            return conversation.getProposal().getProfessional().getAccount().getName();
        }

        return conversation.getProposal().getCompany().getName();
    }
}

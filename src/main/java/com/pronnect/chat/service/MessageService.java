package com.pronnect.chat.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.chat.dto.SendMessageRequest;
import com.pronnect.chat.entity.Conversation;
import com.pronnect.chat.entity.Message;
import com.pronnect.chat.repository.ConversationRepository;
import com.pronnect.chat.repository.MessageRepository;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final AuthenticatedUserService auth;

    @Transactional
    public Message send(UUID conversationId, SendMessageRequest request) {

        Account sender = auth.getCurrentAccount();

        Conversation conversation = getConversationOrThrow(conversationId);

        assertIsParticipant(conversation, sender.getId());

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.content());

        return messageRepository.save(message);
    }

    public Page<Message> getMessages(UUID conversationId, Pageable pageable) {

        Account account = auth.getCurrentAccount();

        Conversation conversation = getConversationOrThrow(conversationId);

        assertIsParticipant(conversation, account.getId());

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId, pageable);
    }

    private Conversation getConversationOrThrow(UUID id) {
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

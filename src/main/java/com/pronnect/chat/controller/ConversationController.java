package com.pronnect.chat.controller;

import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.chat.dto.ConversationResponse;
import com.pronnect.chat.entity.Conversation;
import com.pronnect.chat.mapper.ConversationMapper;
import com.pronnect.chat.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService service;
    private final ConversationMapper mapper;
    private final AuthenticatedUserService auth;

    @GetMapping("/me")
    public ResponseEntity<List<ConversationResponse>> myConversations() {

        UUID accountId = auth.getCurrentAccount().getId();

        List<ConversationResponse> response = service.getMyConversations()
                .stream()
                .map(conversation -> mapper.toResponse(conversation, accountId))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/proposal/{proposalId}")
    public ResponseEntity<ConversationResponse> getByProposal(@PathVariable UUID proposalId) {

        UUID accountId = auth.getCurrentAccount().getId();

        Conversation conversation = service.getByProposalId(proposalId);

        return ResponseEntity.ok(mapper.toResponse(conversation, accountId));
    }
}

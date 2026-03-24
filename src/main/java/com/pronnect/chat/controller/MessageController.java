package com.pronnect.chat.controller;

import com.pronnect.chat.dto.MessageResponse;
import com.pronnect.chat.dto.SendMessageRequest;
import com.pronnect.chat.entity.Message;
import com.pronnect.chat.mapper.MessageMapper;
import com.pronnect.chat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService service;
    private final MessageMapper mapper;

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> send(
            @PathVariable UUID id,
            @RequestBody @Valid SendMessageRequest request
    ) {
        Message message = service.send(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(message));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<Page<MessageResponse>> getMessages(
            @PathVariable UUID id,
            @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<MessageResponse> response = service.getMessages(id, pageable)
                .map(mapper::toResponse);

        return ResponseEntity.ok(response);
    }
}


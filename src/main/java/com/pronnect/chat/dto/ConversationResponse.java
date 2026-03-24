package com.pronnect.chat.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConversationResponse(

        UUID id,
        UUID proposalId,
        String otherPartyName,
        BigDecimal proposalPrice,
        LocalDateTime createdAt

) {}

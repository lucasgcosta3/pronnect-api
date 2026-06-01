package com.pronnect.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BidResponse(

        UUID id,
        UUID projectId,
        UUID professionalId,
        String professionalName,
        String professionalHeadline,
        String professionalAvatarUrl,
        BigDecimal amount,
        int deliveryDays,
        String coverLetter,
        String portfolioUrl,
        String status,
        UUID proposalId,
        LocalDateTime createdAt

) {}

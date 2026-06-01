package com.pronnect.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProjectResponse(

        UUID id,
        UUID companyId,
        String companyName,
        String title,
        String description,
        String paymentType,
        BigDecimal budgetMin,
        BigDecimal budgetMax,
        List<String> skills,
        String status,
        String aiJustification,
        long bidCount,
        LocalDateTime createdAt

) {}

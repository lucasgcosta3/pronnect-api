package com.pronnect.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateProjectRequest(

        @NotBlank String title,
        @NotBlank String description,
        @NotNull String paymentType,
        BigDecimal budgetMin,
        BigDecimal budgetMax,
        List<String> skills,
        String aiJustification

) {}

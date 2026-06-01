package com.pronnect.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateBidRequest(

        @NotNull @Positive BigDecimal amount,
        @NotNull @Positive Integer deliveryDays,
        @NotBlank String coverLetter,
        String portfolioUrl

) {}

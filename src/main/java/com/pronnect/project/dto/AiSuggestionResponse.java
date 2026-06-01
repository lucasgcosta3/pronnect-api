package com.pronnect.project.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.math.BigDecimal;
import java.util.List;

public record AiSuggestionResponse(

        String title,
        String description,
        List<String> skills,
        @JsonAlias({"budgetMin", "budget_min"})
        BigDecimal budgetMin,
        @JsonAlias({"budgetMax", "budget_max"})
        BigDecimal budgetMax,
        @JsonAlias({"paymentType", "payment_type"})
        String paymentType,
        @JsonAlias({"paymentTypeJustification", "payment_type_justification"})
        String paymentTypeJustification

) {}

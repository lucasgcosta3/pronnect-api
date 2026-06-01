package com.pronnect.project.dto;

import jakarta.validation.constraints.NotBlank;

public record AiSuggestionRequest(

        @NotBlank String briefDescription

) {}

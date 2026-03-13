package com.pronnect.professional.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfessionalProfileRequest(

        @NotBlank
        String title,

        String description,

        @Email
        String contactEmail

) {}

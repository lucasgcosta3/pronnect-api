package com.pronnect.professional.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateProfessionalProfileRequest(

        @Size(max = 150)
        String headline,

        String description,

        @Email
        String contactEmail,

        Boolean profileCompleted

) {
}

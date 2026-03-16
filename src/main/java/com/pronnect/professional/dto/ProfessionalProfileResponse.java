package com.pronnect.professional.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProfessionalProfileResponse(

        UUID id,
        String headline,
        String description,
        String contactEmail,
        Boolean profileCompleted,
        LocalDateTime createdAt

) {
}

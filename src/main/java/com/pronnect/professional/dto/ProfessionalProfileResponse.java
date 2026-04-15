package com.pronnect.professional.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public record ProfessionalProfileResponse(

        UUID id,
        String name,
        String headline,
        String description,
        String contactEmail,
        Boolean profileCompleted,
        List<String> skills,
        LocalDateTime createdAt

) {
}

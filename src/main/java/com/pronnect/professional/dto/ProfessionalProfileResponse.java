package com.pronnect.professional.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public record ProfessionalProfileResponse(

        UUID id,
        UUID accountId,
        String name,
        String headline,
        String description,
        String contactEmail,
        String avatarUrl,
        Boolean profileCompleted,
        List<String> skills,
        LocalDateTime createdAt

) {
}

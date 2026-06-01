package com.pronnect.company.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyProfileResponse(

        UUID id,
        UUID accountId,
        String name,
        String description,
        String contactEmail,
        String location,
        String avatarUrl,
        Boolean profileCompleted,
        LocalDateTime createdAt

) {
}


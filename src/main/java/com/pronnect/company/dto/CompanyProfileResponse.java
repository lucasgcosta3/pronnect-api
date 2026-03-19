package com.pronnect.company.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyProfileResponse(

        UUID id,
        String name,
        String description,
        String contactEmail,
        String location,
        Boolean profileCompleted,
        LocalDateTime createdAt

) {
}


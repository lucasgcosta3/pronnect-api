package com.pronnect.company.dto;

import jakarta.validation.constraints.Email;

public record UpdateCompanyProfileRequest(

        String name,

        String description,

        @Email
        String contactEmail,

        String location,

        Boolean profileCompleted

) {
}
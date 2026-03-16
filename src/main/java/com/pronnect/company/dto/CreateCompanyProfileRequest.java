package com.pronnect.company.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCompanyProfileRequest(

        @NotBlank
        String name,

        String description,

        @Email
        String contactEmail,

        String website,

        String location

) {
}

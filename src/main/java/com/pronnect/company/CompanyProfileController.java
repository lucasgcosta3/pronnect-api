package com.pronnect.company;

import com.pronnect.company.dto.CompanyProfileResponse;
import com.pronnect.company.dto.CreateCompanyProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyProfileController {

    private final CompanyProfileService companyProfileService;

    @PostMapping
    public ResponseEntity<CompanyProfileResponse> createProfile(
            @RequestParam UUID accountId,
            @RequestBody @Valid CreateCompanyProfileRequest request
    ) {
        CompanyProfileResponse profile = companyProfileService.createProfile(accountId, request);
        return ResponseEntity.ok(profile);
    }
}

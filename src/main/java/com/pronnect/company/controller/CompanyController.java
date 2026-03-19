package com.pronnect.company.controller;

import com.pronnect.company.entity.CompanyProfile;
import com.pronnect.company.dto.CompanyProfileResponse;
import com.pronnect.company.dto.CreateCompanyProfileRequest;
import com.pronnect.company.dto.UpdateCompanyProfileRequest;
import com.pronnect.company.mapper.CompanyProfileMapper;
import com.pronnect.company.service.CompanyProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyProfileService service;
    private final CompanyProfileMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponse> create(
            @RequestBody @Valid CreateCompanyProfileRequest request
    ) {
        CompanyProfile profile = service.createProfile(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toResponse(profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyProfileResponse> getById(@PathVariable UUID id) {
        CompanyProfile profile = service.getById(id);
        return ResponseEntity.ok(mapper.toResponse(profile));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponse> me() {

        CompanyProfile profile = service.getCurrentCompany();

        return ResponseEntity.ok(mapper.toResponse(profile));
    }

    @PutMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponse> update(
            @RequestBody @Valid UpdateCompanyProfileRequest request
    ) {
        CompanyProfile profile = service.updateProfile(request);
        return ResponseEntity.ok(mapper.toResponse(profile));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> delete() {
        service.deleteProfile();
        return ResponseEntity.noContent().build();
    }
}
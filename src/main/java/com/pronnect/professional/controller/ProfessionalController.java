package com.pronnect.professional.controller;

import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.professional.service.ProfessionalService;
import com.pronnect.professional.dto.AddSkillRequest;
import com.pronnect.professional.dto.CreateProfessionalProfileRequest;
import com.pronnect.professional.dto.ProfessionalProfileResponse;
import com.pronnect.professional.dto.UpdateProfessionalProfileRequest;
import com.pronnect.professional.mapper.ProfessionalMapper;
import com.pronnect.skill.dto.SkillResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
public class ProfessionalController {

    private final ProfessionalService service;
    private final ProfessionalMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<ProfessionalProfileResponse> create(
            @RequestBody @Valid CreateProfessionalProfileRequest request
    ) {
        ProfessionalProfile profile = service.createProfile(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toResponse(profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalProfileResponse> getById(
            @PathVariable UUID id
    ) {
        ProfessionalProfile profile = service.getById(id);
        return ResponseEntity.ok(mapper.toResponse(profile));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<ProfessionalProfileResponse> me() {
        ProfessionalProfile profile = service.getCurrentProfessional();
        return ResponseEntity.ok(mapper.toResponse(profile));
    }

    @PutMapping
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<ProfessionalProfileResponse> update(
            @RequestBody @Valid UpdateProfessionalProfileRequest request
    ) {
        ProfessionalProfile profile = service.updateProfile(request);
        return ResponseEntity.ok(mapper.toResponse(profile));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Void> delete() {
        service.deleteProfile();
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProfessionalProfileResponse>> search(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String text,
            Pageable pageable
    ) {

        Page<ProfessionalProfile> profiles =
                service.searchProfessionals(skill, text, pageable);

        Page<ProfessionalProfileResponse> response =
                profiles.map(mapper::toResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/me/skills")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Void> addSkill(
            @RequestBody @Valid AddSkillRequest request
    ) {
        service.addSkill(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/skills/{skillId}")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Void> removeSkill(
            @PathVariable UUID skillId
    ) {
        service.removeSkill(skillId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/skills")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<List<SkillResponse>> getMySkills() {

        List<SkillResponse> skills = service.getMySkills();

        return ResponseEntity.ok(skills);
    }
}

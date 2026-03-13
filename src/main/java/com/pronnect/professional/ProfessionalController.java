package com.pronnect.professional;

import com.pronnect.professional.dto.ProfessionalProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
public class ProfessionalController {

    private final ProfessionalService service;

    @PostMapping("/profile")
    public ResponseEntity<Void> createProfile(@RequestParam UUID accountId,
        @Valid @RequestBody ProfessionalProfileRequest request) {

        service.createProfile(accountId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public Page<ProfessionalProfile> search(Pageable pageable) {
        return service.searchProfessionals(pageable);
    }
}

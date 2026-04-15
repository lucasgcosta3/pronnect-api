package com.pronnect.proposal.controller;

import com.pronnect.proposal.dto.CreateProposalRequest;
import com.pronnect.proposal.dto.ProposalResponse;
import com.pronnect.proposal.entity.Proposal;
import com.pronnect.proposal.mapper.ProposalMapper;
import com.pronnect.proposal.service.ProposalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/proposals")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService service;
    private final ProposalMapper mapper;

    @GetMapping("/me")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<List<ProposalResponse>> myProposals() {
        List<ProposalResponse> response = service.getMyReceivedProposals()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sent")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<ProposalResponse>> sent() {
        List<ProposalResponse> response = service.getMySentProposals()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ProposalResponse> create(
            @RequestBody @Valid CreateProposalRequest request
    ) {
        Proposal proposal = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(proposal));
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Void> accept(@PathVariable UUID id) {
        service.accept(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Void> reject(@PathVariable UUID id) {
        service.reject(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        service.cancel(id);
        return ResponseEntity.noContent().build();
    }
}

package com.pronnect.servicecontract.controller;

import com.pronnect.servicecontract.dto.ServiceContractResponse;
import com.pronnect.servicecontract.entity.ServiceContract;
import com.pronnect.servicecontract.mapper.ServiceContractMapper;
import com.pronnect.servicecontract.service.ServiceContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ServiceContractController {

    private final ServiceContractService service;
    private final ServiceContractMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<ServiceContractResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(service.getOrThrow(id)));
    }

    @GetMapping("/proposal/{proposalId}")
    public ResponseEntity<ServiceContractResponse> getByProposal(@PathVariable UUID proposalId) {
        ServiceContract contract = service.getByProposalId(proposalId);
        return ResponseEntity.ok(mapper.toResponse(contract));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ServiceContractResponse>> myContracts() {
        List<ServiceContractResponse> response = service.getMyContracts()
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<ServiceContractResponse> complete(@PathVariable UUID id) {
        ServiceContract contract = service.markAsCompleted(id);
        return ResponseEntity.ok(mapper.toResponse(contract));
    }

    @PatchMapping("/{id}/validate")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ServiceContractResponse> validate(@PathVariable UUID id) {
        ServiceContract contract = service.validate(id);
        return ResponseEntity.ok(mapper.toResponse(contract));
    }
}
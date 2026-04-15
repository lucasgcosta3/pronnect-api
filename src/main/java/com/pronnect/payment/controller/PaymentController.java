package com.pronnect.payment.controller;

import com.pronnect.payment.dto.CreatePaymentRequest;
import com.pronnect.payment.dto.PaymentResponse;
import com.pronnect.payment.entity.Payment;
import com.pronnect.payment.mapper.PaymentMapper;
import com.pronnect.payment.repository.PaymentRepository;
import com.pronnect.payment.service.PaymentService;
import com.pronnect.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;
    private final PaymentMapper mapper;
    private final PaymentRepository repository;

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<PaymentResponse> getByContract(@PathVariable UUID contractId) {
        Payment payment = repository.findByServiceContractId(contractId)
                .orElseThrow(() -> new NotFoundException("Payment not found for this contract"));
        return ResponseEntity.ok(mapper.toResponse(payment));
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<PaymentResponse> hold(@RequestBody @Valid CreatePaymentRequest request) {
        Payment payment = service.hold(request.serviceContractId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(payment));
    }

    @PatchMapping("/contract/{contractId}/release")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<PaymentResponse> release(@PathVariable UUID contractId) {
        Payment payment = service.release(contractId);
        return ResponseEntity.ok(mapper.toResponse(payment));
    }
}
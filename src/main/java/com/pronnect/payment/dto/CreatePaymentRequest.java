package com.pronnect.payment.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePaymentRequest(

        @NotNull
        UUID serviceContractId

) {}
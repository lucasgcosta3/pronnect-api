package com.pronnect.proposal.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProposalResponse(

        UUID id,
        UUID companyId,
        UUID professionalId,
        String message,
        BigDecimal price,
        String status,
        UUID contractId

) {}
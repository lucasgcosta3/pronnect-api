package com.pronnect.proposal.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProposalRequest(

        UUID professionalId,
        String message,
        BigDecimal price

) {}

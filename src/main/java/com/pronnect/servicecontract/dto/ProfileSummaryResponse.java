package com.pronnect.servicecontract.dto;

import java.math.BigDecimal;

public record ProfileSummaryResponse(
        int activeContracts,
        String activeStatus,
        BigDecimal escrowBalance
) {}
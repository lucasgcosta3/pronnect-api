package com.pronnect.review.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
    UUID id,
    UUID reviewerAccountId,
    String reviewerName,
    UUID reviewedAccountId,
    UUID serviceContractId,
    int rating,
    String comment,
    LocalDateTime createdAt
) {}

package com.pronnect.review.dto;

import java.util.List;

public record ReviewSummaryResponse(
    double averageRating,
    long totalReviews,
    List<ReviewResponse> reviews
) {}

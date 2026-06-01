package com.pronnect.review.controller;

import com.pronnect.review.dto.CreateReviewRequest;
import com.pronnect.review.dto.ReviewResponse;
import com.pronnect.review.dto.ReviewSummaryResponse;
import com.pronnect.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@RequestBody @Valid CreateReviewRequest request) {
        ReviewResponse response = service.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<ReviewResponse>> getByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(service.getReviewsByAccountId(accountId));
    }

    @GetMapping("/account/{accountId}/summary")
    public ResponseEntity<ReviewSummaryResponse> getSummaryByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(service.getReviewSummaryByAccountId(accountId));
    }
}

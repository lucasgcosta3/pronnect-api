package com.pronnect.review.mapper;

import com.pronnect.review.dto.ReviewResponse;
import com.pronnect.review.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getReviewerAccount().getId(),
                review.getReviewerAccount().getName(),
                review.getReviewedAccount().getId(),
                review.getServiceContract().getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}

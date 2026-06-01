package com.pronnect.review.repository;

import com.pronnect.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    
    List<Review> findByReviewedAccountIdOrderByCreatedAtDesc(UUID reviewedAccountId);
    
    boolean existsByReviewerAccountIdAndReviewedAccountIdAndServiceContractId(UUID reviewerId, UUID reviewedId, UUID contractId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewedAccount.id = :accountId")
    Optional<Double> findAverageRatingByReviewedAccountId(@Param("accountId") UUID accountId);
    
    long countByReviewedAccountId(UUID accountId);
}

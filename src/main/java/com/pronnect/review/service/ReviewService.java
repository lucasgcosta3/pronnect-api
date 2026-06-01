package com.pronnect.review.service;

import com.pronnect.account.entity.Account;
import com.pronnect.account.repository.AccountRepository;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.exception.BusinessException;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.exception.NotFoundException;
import com.pronnect.review.dto.CreateReviewRequest;
import com.pronnect.review.dto.ReviewResponse;
import com.pronnect.review.dto.ReviewSummaryResponse;
import com.pronnect.review.entity.Review;
import com.pronnect.review.mapper.ReviewMapper;
import com.pronnect.review.repository.ReviewRepository;
import com.pronnect.servicecontract.entity.ServiceContract;
import com.pronnect.servicecontract.enums.ServiceContractStatus;
import com.pronnect.servicecontract.repository.ServiceContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;
    private final ServiceContractRepository serviceContractRepository;
    private final AuthenticatedUserService auth;
    private final ReviewMapper mapper;

    @Transactional
    public ReviewResponse createReview(CreateReviewRequest request) {
        Account reviewer = auth.getCurrentAccount();

        Account reviewed = accountRepository.findById(request.reviewedAccountId())
                .orElseThrow(() -> new NotFoundException("Reviewed account not found"));

        if (reviewer.getId().equals(reviewed.getId())) {
            throw new BusinessException("You cannot review yourself");
        }

        ServiceContract contract = serviceContractRepository.findById(request.serviceContractId())
                .orElseThrow(() -> new NotFoundException("Service contract not found"));

        if (contract.getStatus() != ServiceContractStatus.VALIDATED) {
            throw new BusinessException("Service contract must be VALIDATED to leave a review");
        }

        boolean isReviewerCompany = contract.getProposal().getCompany().getAccount().getId().equals(reviewer.getId());
        boolean isReviewerProfessional = contract.getProposal().getProfessional().getAccount().getId().equals(reviewer.getId());

        if (!isReviewerCompany && !isReviewerProfessional) {
            throw new ForbiddenException("You are not part of this service contract");
        }

        boolean isReviewedCompany = contract.getProposal().getCompany().getAccount().getId().equals(reviewed.getId());
        boolean isReviewedProfessional = contract.getProposal().getProfessional().getAccount().getId().equals(reviewed.getId());

        if (!isReviewedCompany && !isReviewedProfessional) {
            throw new BusinessException("The reviewed account is not part of this service contract");
        }

        if (reviewRepository.existsByReviewerAccountIdAndReviewedAccountIdAndServiceContractId(
                reviewer.getId(), reviewed.getId(), contract.getId())) {
            throw new BusinessException("You have already reviewed this account for this contract");
        }

        Review review = new Review();
        review.setReviewerAccount(reviewer);
        review.setReviewedAccount(reviewed);
        review.setServiceContract(contract);
        review.setRating(request.rating());
        review.setComment(request.comment());

        review = reviewRepository.save(review);
        return mapper.toResponse(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByAccountId(UUID accountId) {
        return reviewRepository.findByReviewedAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewSummaryResponse getReviewSummaryByAccountId(UUID accountId) {
        List<ReviewResponse> reviews = getReviewsByAccountId(accountId);
        double average = reviewRepository.findAverageRatingByReviewedAccountId(accountId).orElse(0.0);
        long total = reviewRepository.countByReviewedAccountId(accountId);
        
        return new ReviewSummaryResponse(average, total, reviews);
    }
}

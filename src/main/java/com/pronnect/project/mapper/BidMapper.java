package com.pronnect.project.mapper;

import com.pronnect.project.dto.BidResponse;
import com.pronnect.project.entity.ProjectBid;
import org.springframework.stereotype.Component;

@Component
public class BidMapper {

    public BidResponse toResponse(ProjectBid bid) {
        var professional = bid.getProfessional();
        String name = professional.getAccount().getName();
        String headline = professional.getHeadline();
        String avatarUrl = professional.getAvatarUrl();

        return new BidResponse(
                bid.getId(),
                bid.getProject().getId(),
                professional.getId(),
                name,
                headline,
                avatarUrl,
                bid.getAmount(),
                bid.getDeliveryDays(),
                bid.getCoverLetter(),
                bid.getPortfolioUrl(),
                bid.getStatus().name(),
                bid.getProposal() != null ? bid.getProposal().getId() : null,
                bid.getCreatedAt()
        );
    }
}

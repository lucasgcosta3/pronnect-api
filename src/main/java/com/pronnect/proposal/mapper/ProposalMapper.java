package com.pronnect.proposal.mapper;

import com.pronnect.proposal.dto.ProposalResponse;
import com.pronnect.proposal.entity.Proposal;
import com.pronnect.servicecontract.repository.ServiceContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProposalMapper {

    private final ServiceContractRepository serviceContractRepository;

    public ProposalResponse toResponse(Proposal proposal) {
        UUID contractId = serviceContractRepository
                .findByProposalId(proposal.getId())
                .map(c -> c.getId())
                .orElse(null);

        return new ProposalResponse(
                proposal.getId(),
                proposal.getCompany().getId(),
                proposal.getProfessional().getId(),
                proposal.getMessage(),
                proposal.getPrice(),
                proposal.getStatus().name(),
                contractId
        );
    }
}
package com.pronnect.proposal.mapper;

import com.pronnect.proposal.dto.ProposalResponse;
import com.pronnect.proposal.entity.Proposal;
import com.pronnect.servicecontract.entity.ServiceContract;
import com.pronnect.servicecontract.enums.ServiceContractStatus;
import com.pronnect.servicecontract.repository.ServiceContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProposalMapper {

    private final ServiceContractRepository serviceContractRepository;

    public ProposalResponse toResponse(Proposal proposal) {
        ServiceContract contract = serviceContractRepository
                .findByProposalId(proposal.getId())
                .orElse(null);

        UUID contractId = contract != null ? contract.getId() : null;
        boolean isFinished = contract != null && contract.getStatus() == ServiceContractStatus.VALIDATED;

        return new ProposalResponse(
                proposal.getId(),
                proposal.getCompany().getId(),
                proposal.getProfessional().getId(),
                proposal.getMessage(),
                proposal.getPrice(),
                proposal.getStatus().name(),
                contractId,
                isFinished
        );
    }
}
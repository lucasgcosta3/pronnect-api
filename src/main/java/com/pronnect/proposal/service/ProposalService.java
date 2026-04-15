package com.pronnect.proposal.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.chat.service.ConversationService;
import com.pronnect.company.entity.CompanyProfile;
import com.pronnect.company.repository.CompanyRepository;
import com.pronnect.exception.BusinessException;
import com.pronnect.exception.NotFoundException;
import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.professional.repository.ProfessionalRepository;
import com.pronnect.proposal.dto.CreateProposalRequest;
import com.pronnect.proposal.entity.Proposal;
import com.pronnect.proposal.enums.ProposalStatus;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.proposal.repository.ProposalRepository;
import com.pronnect.servicecontract.service.ServiceContractService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository repository;
    private final CompanyRepository companyRepository;
    private final ProfessionalRepository professionalRepository;
    private final ConversationService conversationService;
    private final ServiceContractService serviceContractService;
    private final AuthenticatedUserService auth;

    @Transactional
    public Proposal create(CreateProposalRequest request) {

        Account account = auth.getCurrentAccount();

        CompanyProfile company = companyRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        ProfessionalProfile professional = professionalRepository
                .findById(request.professionalId())
                .orElseThrow(() -> new NotFoundException("Professional not found"));

        boolean alreadyExists = repository
                .existsByCompanyIdAndProfessionalIdAndStatusIn(
                        company.getId(),
                        professional.getId(),
                        List.of(ProposalStatus.PENDING, ProposalStatus.ACCEPTED)
                );

        if (alreadyExists) {
            throw new BusinessException("You already have an active proposal for this professional");
        }

        Proposal proposal = new Proposal();

        proposal.setCompany(company);
        proposal.setProfessional(professional);
        proposal.setMessage(request.message());
        proposal.setPrice(request.price());
        proposal.setStatus(ProposalStatus.PENDING);

        return repository.save(proposal);
    }

    public List<Proposal> getMyReceivedProposals() {

        Account account = auth.getCurrentAccount();

        ProfessionalProfile profile = professionalRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Professional profile not found"));

        return repository.findByProfessionalId(profile.getId());
    }

    public List<Proposal> getMySentProposals() {

        Account account = auth.getCurrentAccount();

        CompanyProfile company = companyRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        return repository.findByCompanyId(company.getId());
    }

    @Transactional
    public void accept(UUID proposalId) {

        Proposal proposal = getProposalOrThrow(proposalId);

        Account account = auth.getCurrentAccount();

        if (!proposal.getProfessional().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("You cannot accept this proposal");
        }

        if (proposal.getStatus() != ProposalStatus.PENDING) {
            throw new BusinessException("Proposal already processed");
        }

        proposal.setStatus(ProposalStatus.ACCEPTED);
        conversationService.createForProposal(proposal);
        serviceContractService.createForProposal(proposal);
    }

    @Transactional
    public void reject(UUID proposalId) {

        Proposal proposal = getProposalOrThrow(proposalId);

        Account account = auth.getCurrentAccount();

        if (!proposal.getProfessional().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("You cannot reject this proposal");
        }

        if (proposal.getStatus() != ProposalStatus.PENDING) {
            throw new BusinessException("Proposal already processed");
        }

        proposal.setStatus(ProposalStatus.REJECTED);
    }

    @Transactional
    public void cancel(UUID proposalId) {

        Proposal proposal = getProposalOrThrow(proposalId);

        Account account = auth.getCurrentAccount();

        if (!proposal.getCompany().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("You cannot cancel this proposal");
        }

        if (proposal.getStatus() != ProposalStatus.PENDING) {
            throw new BusinessException("Only pending proposals can be cancelled");
        }

        proposal.setStatus(ProposalStatus.CANCELLED);
    }

    private Proposal getProposalOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proposal not found"));
    }
}

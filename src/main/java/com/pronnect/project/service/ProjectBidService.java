package com.pronnect.project.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.chat.service.ConversationService;
import com.pronnect.company.entity.CompanyProfile;
import com.pronnect.company.repository.CompanyRepository;
import com.pronnect.exception.BusinessException;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.exception.NotFoundException;
import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.professional.repository.ProfessionalRepository;
import com.pronnect.project.dto.CreateBidRequest;
import com.pronnect.project.entity.Project;
import com.pronnect.project.entity.ProjectBid;
import com.pronnect.project.enums.BidStatus;
import com.pronnect.project.enums.ProjectStatus;
import com.pronnect.project.repository.ProjectBidRepository;
import com.pronnect.project.repository.ProjectRepository;
import com.pronnect.proposal.entity.Proposal;
import com.pronnect.proposal.enums.ProposalStatus;
import com.pronnect.proposal.repository.ProposalRepository;
import com.pronnect.servicecontract.service.ServiceContractService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectBidService {

    private final ProjectBidRepository bidRepository;
    private final ProjectRepository projectRepository;
    private final ProfessionalRepository professionalRepository;
    private final CompanyRepository companyRepository;
    private final AuthenticatedUserService auth;
    private final ProposalRepository proposalRepository;
    private final ConversationService conversationService;
    private final ServiceContractService serviceContractService;

    @Transactional
    public ProjectBid createBid(UUID projectId, CreateBidRequest request) {

        Account account = auth.getCurrentAccount();

        ProfessionalProfile professional = professionalRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Professional profile not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new BusinessException("This project is not accepting bids");
        }

        if (bidRepository.existsByProjectIdAndProfessionalId(projectId, professional.getId())) {
            throw new BusinessException("You already placed a bid on this project");
        }

        ProjectBid bid = ProjectBid.builder()
                .project(project)
                .professional(professional)
                .amount(request.amount())
                .deliveryDays(request.deliveryDays())
                .coverLetter(request.coverLetter())
                .portfolioUrl(request.portfolioUrl())
                .status(BidStatus.PENDING)
                .build();

        return bidRepository.save(bid);
    }

    public List<ProjectBid> getBidsForProject(UUID projectId) {

        Account account = auth.getCurrentAccount();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        CompanyProfile company = companyRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        if (!project.getCompany().getId().equals(company.getId())) {
            throw new ForbiddenException("You can only view bids for your own projects");
        }

        return bidRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
    }

    public List<ProjectBid> getMyBids() {

        Account account = auth.getCurrentAccount();

        ProfessionalProfile professional = professionalRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Professional profile not found"));

        return bidRepository.findByProfessionalIdOrderByCreatedAtDesc(professional.getId());
    }

    @Transactional
    public void acceptBid(UUID bidId) {

        ProjectBid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new NotFoundException("Bid not found"));

        Account account = auth.getCurrentAccount();

        CompanyProfile company = companyRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        if (!bid.getProject().getCompany().getId().equals(company.getId())) {
            throw new ForbiddenException("You cannot accept this bid");
        }

        if (bid.getStatus() != BidStatus.PENDING) {
            throw new BusinessException("Bid already processed");
        }

        bid.setStatus(BidStatus.ACCEPTED);

        // Close the project after accepting a bid
        Project project = bid.getProject();
        project.setStatus(ProjectStatus.IN_PROGRESS);

        // Create direct proposal
        Proposal proposal = new Proposal();
        proposal.setCompany(company);
        proposal.setProfessional(bid.getProfessional());
        proposal.setMessage("Proposta para o projeto: " + project.getTitle() + "\n\n" + bid.getCoverLetter());
        proposal.setPrice(bid.getAmount());
        proposal.setStatus(ProposalStatus.ACCEPTED);
        proposal = proposalRepository.save(proposal);

        bid.setProposal(proposal);

        // Initialize Chat and Service Contract
        conversationService.createForProposal(proposal);
        serviceContractService.createForProposal(proposal);
    }

    @Transactional
    public void rejectBid(UUID bidId) {

        ProjectBid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new NotFoundException("Bid not found"));

        Account account = auth.getCurrentAccount();

        CompanyProfile company = companyRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        if (!bid.getProject().getCompany().getId().equals(company.getId())) {
            throw new ForbiddenException("You cannot reject this bid");
        }

        if (bid.getStatus() != BidStatus.PENDING) {
            throw new BusinessException("Bid already processed");
        }

        bid.setStatus(BidStatus.REJECTED);
    }
}

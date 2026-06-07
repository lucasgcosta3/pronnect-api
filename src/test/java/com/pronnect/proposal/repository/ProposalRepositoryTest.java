package com.pronnect.proposal.repository;

import com.pronnect.account.entity.Account;
import com.pronnect.account.enums.AccountRole;
import com.pronnect.account.repository.AccountRepository;
import com.pronnect.company.entity.CompanyProfile;
import com.pronnect.company.repository.CompanyRepository;
import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.professional.repository.ProfessionalRepository;
import com.pronnect.proposal.entity.Proposal;
import com.pronnect.proposal.enums.ProposalStatus;
import com.pronnect.servicecontract.entity.ServiceContract;
import com.pronnect.servicecontract.enums.ServiceContractStatus;
import com.pronnect.servicecontract.repository.ServiceContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProposalRepositoryTest {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private ServiceContractRepository serviceContractRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private AccountRepository accountRepository;

    private CompanyProfile company;
    private ProfessionalProfile professional;

    @BeforeEach
    void setUp() {
        Account companyAccount = Account.builder()
                .name("Test Company Owner")
                .email("company-" + UUID.randomUUID() + "@test.com")
                .role(AccountRole.COMPANY)
                .build();
        accountRepository.save(companyAccount);

        company = CompanyProfile.builder()
                .account(companyAccount)
                .name("Test Company")
                .profileCompleted(true)
                .build();
        companyRepository.save(company);

        Account professionalAccount = Account.builder()
                .name("Test Professional User")
                .email("professional-" + UUID.randomUUID() + "@test.com")
                .role(AccountRole.PROFESSIONAL)
                .build();
        accountRepository.save(professionalAccount);

        professional = ProfessionalProfile.builder()
                .account(professionalAccount)
                .profileCompleted(true)
                .build();
        professionalRepository.save(professional);
    }

    @Test
    void shouldReturnFalseWhenNoProposalsExist() {
        boolean exists = proposalRepository.existsActiveProposal(company.getId(), professional.getId());
        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnTrueWhenPendingProposalExists() {
        Proposal proposal = new Proposal();
        proposal.setCompany(company);
        proposal.setProfessional(professional);
        proposal.setStatus(ProposalStatus.PENDING);
        proposal.setPrice(BigDecimal.valueOf(1000));
        proposalRepository.save(proposal);

        boolean exists = proposalRepository.existsActiveProposal(company.getId(), professional.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnTrueWhenAcceptedProposalHasNoContract() {
        Proposal proposal = new Proposal();
        proposal.setCompany(company);
        proposal.setProfessional(professional);
        proposal.setStatus(ProposalStatus.ACCEPTED);
        proposalRepository.save(proposal);

        boolean exists = proposalRepository.existsActiveProposal(company.getId(), professional.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnTrueWhenAcceptedProposalHasContractInProgress() {
        Proposal proposal = new Proposal();
        proposal.setCompany(company);
        proposal.setProfessional(professional);
        proposal.setStatus(ProposalStatus.ACCEPTED);
        proposalRepository.save(proposal);

        ServiceContract contract = new ServiceContract();
        contract.setProposal(proposal);
        contract.setStatus(ServiceContractStatus.IN_PROGRESS);
        serviceContractRepository.save(contract);

        boolean exists = proposalRepository.existsActiveProposal(company.getId(), professional.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnTrueWhenAcceptedProposalHasContractCompletedButNotValidated() {
        Proposal proposal = new Proposal();
        proposal.setCompany(company);
        proposal.setProfessional(professional);
        proposal.setStatus(ProposalStatus.ACCEPTED);
        proposalRepository.save(proposal);

        ServiceContract contract = new ServiceContract();
        contract.setProposal(proposal);
        contract.setStatus(ServiceContractStatus.COMPLETED);
        serviceContractRepository.save(contract);

        boolean exists = proposalRepository.existsActiveProposal(company.getId(), professional.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenAcceptedProposalHasContractValidated() {
        Proposal proposal = new Proposal();
        proposal.setCompany(company);
        proposal.setProfessional(professional);
        proposal.setStatus(ProposalStatus.ACCEPTED);
        proposalRepository.save(proposal);

        ServiceContract contract = new ServiceContract();
        contract.setProposal(proposal);
        contract.setStatus(ServiceContractStatus.VALIDATED);
        serviceContractRepository.save(contract);

        boolean exists = proposalRepository.existsActiveProposal(company.getId(), professional.getId());
        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnFalseWhenProposalsAreRejectedOrCancelled() {
        Proposal proposal1 = new Proposal();
        proposal1.setCompany(company);
        proposal1.setProfessional(professional);
        proposal1.setStatus(ProposalStatus.REJECTED);
        proposalRepository.save(proposal1);

        Proposal proposal2 = new Proposal();
        proposal2.setCompany(company);
        proposal2.setProfessional(professional);
        proposal2.setStatus(ProposalStatus.CANCELLED);
        proposalRepository.save(proposal2);

        boolean exists = proposalRepository.existsActiveProposal(company.getId(), professional.getId());
        assertThat(exists).isFalse();
    }
}

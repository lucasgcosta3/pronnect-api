package com.pronnect.servicecontract.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.exception.BusinessException;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.exception.NotFoundException;
import com.pronnect.payment.entity.Payment;
import com.pronnect.payment.enums.PaymentStatus;
import com.pronnect.payment.repository.PaymentRepository;
import com.pronnect.proposal.entity.Proposal;
import com.pronnect.servicecontract.dto.ProfileSummaryResponse;
import com.pronnect.servicecontract.entity.ServiceContract;
import com.pronnect.servicecontract.enums.ServiceContractStatus;
import com.pronnect.servicecontract.repository.ServiceContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceContractService {

    private final ServiceContractRepository repository;
    private final PaymentRepository paymentRepository;
    private final AuthenticatedUserService auth;

    @Transactional
    public ServiceContract createForProposal(Proposal proposal) {
        ServiceContract contract = new ServiceContract();
        contract.setProposal(proposal);
        contract.setStatus(ServiceContractStatus.IN_PROGRESS);
        return repository.save(contract);
    }

    @Transactional(readOnly = true)
    public ServiceContract getByProposalId(UUID proposalId) {
        return repository.findByProposalId(proposalId)
                .orElseThrow(() -> new NotFoundException("Service contract not found for this proposal"));
    }

    @Transactional(readOnly = true)
    public List<ServiceContract> getMyContracts() {
        Account account = auth.getCurrentAccount();
        return repository.findAllByAccountId(account.getId());
    }

    @Transactional(readOnly = true)
    public ProfileSummaryResponse getProfileSummary(UUID accountId) {
        List<ServiceContract> contracts = repository.findAllByAccountId(accountId);

        int activeContracts = (int) contracts.stream()
                .filter(c -> c.getStatus() == ServiceContractStatus.IN_PROGRESS || c.getStatus() == ServiceContractStatus.COMPLETED)
                .count();

        BigDecimal escrowBalance = contracts.stream()
                .map(contract -> paymentRepository.findByServiceContractId(contract.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(payment -> payment.getStatus() == PaymentStatus.HELD)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String activeStatus;
        boolean hasHeld = escrowBalance.compareTo(BigDecimal.ZERO) > 0;
        boolean hasInProgress = contracts.stream().anyMatch(c -> c.getStatus() == ServiceContractStatus.IN_PROGRESS);
        boolean hasCompleted = contracts.stream().anyMatch(c -> c.getStatus() == ServiceContractStatus.COMPLETED);

        if (hasHeld) {
            activeStatus = "Pagamento em Escrow";
        } else if (hasInProgress) {
            activeStatus = "Em andamento";
        } else if (hasCompleted) {
            activeStatus = "Aguardando validação";
        } else {
            activeStatus = "Nenhum projeto em andamento";
        }

        return new ProfileSummaryResponse(activeContracts, activeStatus, escrowBalance);
    }

    @Transactional
    public ServiceContract markAsCompleted(UUID contractId) {
        Account account = auth.getCurrentAccount();
        ServiceContract contract = getOrThrow(contractId);

        if (!contract.getProposal().getProfessional().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("Only the professional can mark the service as completed");
        }

        if (contract.getStatus() != ServiceContractStatus.IN_PROGRESS) {
            throw new BusinessException("Service is not in progress");
        }

        contract.setStatus(ServiceContractStatus.COMPLETED);
        contract.setCompletedAt(LocalDateTime.now());

        return repository.save(contract);
    }

    @Transactional
    public ServiceContract validate(UUID contractId) {
        Account account = auth.getCurrentAccount();
        ServiceContract contract = getOrThrow(contractId);

        if (!contract.getProposal().getCompany().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("Only the company can validate the service");
        }

        if (contract.getStatus() != ServiceContractStatus.COMPLETED) {
            throw new BusinessException("Service has not been marked as completed yet");
        }

        contract.setStatus(ServiceContractStatus.VALIDATED);
        contract.setValidatedAt(LocalDateTime.now());

        return repository.save(contract);
    }

    @Transactional(readOnly = true)
    public ServiceContract getOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service contract not found"));
    }
}

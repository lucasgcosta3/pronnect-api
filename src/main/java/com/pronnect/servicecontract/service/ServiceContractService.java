package com.pronnect.servicecontract.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.exception.BusinessException;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.exception.NotFoundException;
import com.pronnect.proposal.entity.Proposal;
import com.pronnect.servicecontract.entity.ServiceContract;
import com.pronnect.servicecontract.enums.ServiceContractStatus;
import com.pronnect.servicecontract.repository.ServiceContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceContractService {

    private final ServiceContractRepository repository;
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

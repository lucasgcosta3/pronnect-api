package com.pronnect.servicecontract.repository;

import com.pronnect.servicecontract.entity.ServiceContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceContractRepository extends JpaRepository<ServiceContract, UUID> {
    Optional<ServiceContract> findByProposalId(UUID proposalId);

    @Query("SELECT sc FROM ServiceContract sc WHERE " +
           "sc.proposal.company.account.id = :accountId OR " +
           "sc.proposal.professional.account.id = :accountId")
    List<ServiceContract> findAllByAccountId(@Param("accountId") UUID accountId);
}

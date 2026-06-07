package com.pronnect.proposal.repository;

import com.pronnect.proposal.entity.Proposal;
import com.pronnect.proposal.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProposalRepository extends JpaRepository<Proposal, UUID> {

    List<Proposal> findByProfessionalId(UUID professionalId);

    List<Proposal> findByCompanyId(UUID companyId);

    boolean existsByCompanyIdAndProfessionalIdAndStatusIn(
            UUID companyId,
            UUID professionalId,
            List<ProposalStatus> status
    );

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Proposal p " +
           "LEFT JOIN ServiceContract sc ON sc.proposal = p " +
           "WHERE p.company.id = :companyId " +
           "AND p.professional.id = :professionalId " +
           "AND (p.status = com.pronnect.proposal.enums.ProposalStatus.PENDING OR " +
           "(p.status = com.pronnect.proposal.enums.ProposalStatus.ACCEPTED AND (sc IS NULL OR sc.status != com.pronnect.servicecontract.enums.ServiceContractStatus.VALIDATED)))")
    boolean existsActiveProposal(
            @Param("companyId") UUID companyId,
            @Param("professionalId") UUID professionalId
    );

}

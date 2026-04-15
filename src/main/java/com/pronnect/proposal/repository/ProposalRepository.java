package com.pronnect.proposal.repository;

import com.pronnect.proposal.entity.Proposal;
import com.pronnect.proposal.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

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

}

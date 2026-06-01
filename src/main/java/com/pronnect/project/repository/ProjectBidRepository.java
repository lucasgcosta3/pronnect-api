package com.pronnect.project.repository;

import com.pronnect.project.entity.ProjectBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProjectBidRepository extends JpaRepository<ProjectBid, UUID> {

    List<ProjectBid> findByProjectIdOrderByCreatedAtDesc(UUID projectId);

    List<ProjectBid> findByProfessionalIdOrderByCreatedAtDesc(UUID professionalId);

    boolean existsByProjectIdAndProfessionalId(UUID projectId, UUID professionalId);

    long countByProjectId(UUID projectId);

    Optional<ProjectBid> findByProposalId(UUID proposalId);
}

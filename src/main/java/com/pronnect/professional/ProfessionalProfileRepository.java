package com.pronnect.professional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfessionalProfileRepository extends JpaRepository<ProfessionalProfile, UUID> {

    Optional<ProfessionalProfile> findByAccountId(UUID accountId);

    boolean existsByAccountId(UUID accountId);

    Page<ProfessionalProfile> findByProfileCompletedTrue(Pageable pageable);
}

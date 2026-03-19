package com.pronnect.professional.repository;

import com.pronnect.professional.entity.ProfessionalProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfessionalRepository extends JpaRepository<ProfessionalProfile, UUID> {

    @Query("""
        SELECT DISTINCT p
        FROM ProfessionalProfile p
        LEFT JOIN p.skills ps
        LEFT JOIN ps.skill s
        WHERE p.profileCompleted = true
        AND (
            LOWER(p.headline) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))
        )
    """)
    Page<ProfessionalProfile> search(@Param("query") String query, Pageable pageable);

    Optional<ProfessionalProfile> findByAccountId(UUID accountId);

    boolean existsByAccountId(UUID accountId);

    Page<ProfessionalProfile> findByProfileCompletedTrue(Pageable pageable);
}

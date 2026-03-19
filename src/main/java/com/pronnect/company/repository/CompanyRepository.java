package com.pronnect.company.repository;

import com.pronnect.company.entity.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyProfile, UUID> {

    Optional<CompanyProfile> findByAccountId(UUID accountId);

    boolean existsByAccountId(UUID accountId);
}

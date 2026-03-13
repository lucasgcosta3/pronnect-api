package com.pronnect.professionalSkill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfessionalSkillRepository extends JpaRepository<ProfessionalSkill, ProfessionalSkillId> {

    boolean existsByProfessionalProfileIdAndSkillId(UUID professionalProfileId, UUID skillId);

    List<ProfessionalSkill> findByProfessionalProfileId(UUID profileId);

}

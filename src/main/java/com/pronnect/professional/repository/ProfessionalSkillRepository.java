package com.pronnect.professional.repository;

import com.pronnect.professional.entity.ProfessionalSkill;
import com.pronnect.professional.entity.ProfessionalSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfessionalSkillRepository extends JpaRepository<ProfessionalSkill, ProfessionalSkillId> {

    boolean existsByProfessionalProfileIdAndSkillId(UUID profileId, UUID skillId);

    void deleteByProfessionalProfileIdAndSkillId(UUID profileId, UUID skillId);

    List<ProfessionalSkill> findByProfessionalProfileId(UUID profileId);

}

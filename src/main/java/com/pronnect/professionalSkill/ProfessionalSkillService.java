package com.pronnect.professionalSkill;

import com.pronnect.professional.ProfessionalProfile;
import com.pronnect.professional.ProfessionalProfileRepository;
import com.pronnect.skill.Skill;
import com.pronnect.skill.SkillRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalSkillService {

    private final ProfessionalProfileRepository professionalProfileRepository;
    private final SkillRepository skillRepository;
    private final ProfessionalSkillRepository professionalSkillRepository;

    @Transactional
    public void addSkills(UUID professionalProfileId, List<UUID> skillIds) {

        ProfessionalProfile profile = professionalProfileRepository
                .findById(professionalProfileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        for (UUID skillId : skillIds) {

            boolean alreadyExists =
                    professionalSkillRepository
                            .existsByProfessionalProfileIdAndSkillId(professionalProfileId, skillId);

            if (alreadyExists) {
                continue;
            }

            Skill skill = skillRepository
                    .findById(skillId)
                    .orElseThrow(() -> new RuntimeException("Skill not found"));

            ProfessionalSkill relation = ProfessionalSkill.builder()
                    .id(new ProfessionalSkillId(professionalProfileId, skillId))
                    .professionalProfile(profile)
                    .skill(skill)
                    .build();

            professionalSkillRepository.save(relation);
        }
    }
}

package com.pronnect.professional.mapper;

import com.pronnect.professional.dto.ProfessionalProfileResponse;
import com.pronnect.professional.entity.ProfessionalProfile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfessionalMapper {

    public ProfessionalProfileResponse toResponse(ProfessionalProfile entity) {

        List<String> skills = entity.getSkills().stream()
                .map(ps -> ps.getSkill().getName())
                .toList();

        return new ProfessionalProfileResponse(
                entity.getId(),
                entity.getHeadline(),
                entity.getDescription(),
                entity.getContactEmail(),
                entity.isProfileCompleted(),
                skills,
                entity.getCreatedAt()
        );
    }
}
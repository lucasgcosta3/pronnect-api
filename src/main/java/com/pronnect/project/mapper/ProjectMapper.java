package com.pronnect.project.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pronnect.project.dto.ProjectResponse;
import com.pronnect.project.entity.Project;
import com.pronnect.project.repository.ProjectBidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ProjectBidRepository bidRepository;
    private final ObjectMapper objectMapper;

    public ProjectResponse toResponse(Project project) {
        List<String> skillsList = parseSkills(project.getSkills());
        long bidCount = bidRepository.countByProjectId(project.getId());

        return new ProjectResponse(
                project.getId(),
                project.getCompany().getId(),
                project.getCompany().getName(),
                project.getTitle(),
                project.getDescription(),
                project.getPaymentType().name(),
                project.getBudgetMin(),
                project.getBudgetMax(),
                skillsList,
                project.getStatus().name(),
                project.getAiJustification(),
                bidCount,
                project.getCreatedAt()
        );
    }

    private List<String> parseSkills(String skillsJson) {
        if (skillsJson == null || skillsJson.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(skillsJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }
}

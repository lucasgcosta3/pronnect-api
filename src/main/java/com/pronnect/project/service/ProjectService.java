package com.pronnect.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.company.entity.CompanyProfile;
import com.pronnect.company.repository.CompanyRepository;
import com.pronnect.exception.BusinessException;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.exception.NotFoundException;
import com.pronnect.project.dto.CreateProjectRequest;
import com.pronnect.project.entity.Project;
import com.pronnect.project.enums.PaymentType;
import com.pronnect.project.enums.ProjectStatus;
import com.pronnect.project.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;
    private final CompanyRepository companyRepository;
    private final AuthenticatedUserService auth;
    private final ObjectMapper objectMapper;

    @Transactional
    public Project create(CreateProjectRequest request) {

        Account account = auth.getCurrentAccount();

        CompanyProfile company = companyRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        String skillsJson;
        try {
            skillsJson = request.skills() != null
                    ? objectMapper.writeValueAsString(request.skills())
                    : "[]";
        } catch (Exception e) {
            skillsJson = "[]";
        }

        Project project = Project.builder()
                .company(company)
                .title(request.title())
                .description(request.description())
                .paymentType(PaymentType.valueOf(request.paymentType()))
                .budgetMin(request.budgetMin())
                .budgetMax(request.budgetMax())
                .skills(skillsJson)
                .status(ProjectStatus.OPEN)
                .aiJustification(request.aiJustification())
                .build();

        return repository.save(project);
    }

    public Page<Project> getAll(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return repository.findByStatusAndTitleContainingIgnoreCase(
                    ProjectStatus.OPEN, search.trim(), pageable);
        }
        return repository.findByStatus(ProjectStatus.OPEN, pageable);
    }

    public Project getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }

    public List<Project> getMyProjects() {
        Account account = auth.getCurrentAccount();

        CompanyProfile company = companyRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        return repository.findByCompanyIdOrderByCreatedAtDesc(company.getId());
    }

    @Transactional
    public void close(UUID projectId) {

        Project project = getById(projectId);
        Account account = auth.getCurrentAccount();

        CompanyProfile company = companyRepository
                .findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        if (!project.getCompany().getId().equals(company.getId())) {
            throw new ForbiddenException("You cannot close this project");
        }

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new BusinessException("Only open projects can be closed");
        }

        project.setStatus(ProjectStatus.CLOSED);
    }
}

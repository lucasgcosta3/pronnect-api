package com.pronnect.project.repository;

import com.pronnect.project.entity.Project;
import com.pronnect.project.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    List<Project> findByCompanyIdOrderByCreatedAtDesc(UUID companyId);

    Page<Project> findByStatusAndTitleContainingIgnoreCase(ProjectStatus status, String title, Pageable pageable);
}

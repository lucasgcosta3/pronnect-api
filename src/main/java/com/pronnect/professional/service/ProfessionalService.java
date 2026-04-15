package com.pronnect.professional.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.exception.NotFoundException;
import com.pronnect.exception.ProfileAlreadyExistsException;
import com.pronnect.exception.SkillAlreadyAddedException;
import com.pronnect.professional.dto.AddSkillRequest;
import com.pronnect.professional.dto.CreateProfessionalProfileRequest;
import com.pronnect.professional.dto.UpdateProfessionalProfileRequest;
import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.professional.entity.ProfessionalSkill;
import com.pronnect.professional.entity.ProfessionalSkillId;
import com.pronnect.professional.repository.ProfessionalRepository;
import com.pronnect.professional.repository.ProfessionalSkillRepository;
import com.pronnect.professional.spec.ProfessionalSpecification;
import com.pronnect.skill.dto.SkillResponse;
import com.pronnect.skill.entity.Skill;
import com.pronnect.skill.mapper.SkillMapper;
import com.pronnect.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository repository;
    private final ProfessionalSkillRepository professionalSkillRepository;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public ProfessionalProfile createProfile(CreateProfessionalProfileRequest request) {

        Account account = authenticatedUserService.getCurrentAccount();

        if (repository.existsByAccountId(account.getId())) {
            throw new ProfileAlreadyExistsException("Professional profile already exists");
        }

        // Update account name if provided
        if (request.name() != null && !request.name().isBlank()) {
            account.setName(request.name());
        }

        ProfessionalProfile profile = ProfessionalProfile.builder()
                .account(account)
                .headline(request.headline())
                .description(request.description())
                .contactEmail(request.contactEmail())
                .profileCompleted(false)
                .build();

        return repository.save(profile);
    }

    @Transactional(readOnly = true)
    public ProfessionalProfile getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Professional profile not found"));
    }

    @Transactional(readOnly = true)
    public ProfessionalProfile getCurrentProfessional() {
        Account account = authenticatedUserService.getCurrentAccount();

        return repository.findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Professional profile not found"));
    }

    @Transactional
    public ProfessionalProfile updateProfile(UpdateProfessionalProfileRequest request) {
        ProfessionalProfile profile = getCurrentProfessional();

        // Update account name if provided
        if (request.name() != null && !request.name().isBlank()) {
            profile.getAccount().setName(request.name());
        }

        profile.setHeadline(request.headline());
        profile.setDescription(request.description());
        profile.setContactEmail(request.contactEmail());
        profile.setProfileCompleted(isProfileComplete(profile));

        return repository.save(profile);
    }

    private boolean isProfileComplete(ProfessionalProfile profile) {
        boolean hasRequiredFields =
                profile.getHeadline() != null && !profile.getHeadline().isBlank()
                && profile.getDescription() != null && !profile.getDescription().isBlank()
                && profile.getContactEmail() != null && !profile.getContactEmail().isBlank();

        boolean hasSkills = !profile.getSkills().isEmpty();

        return hasRequiredFields && hasSkills;
    }

    @Transactional
    public void deleteProfile() {
        ProfessionalProfile profile = getCurrentProfessional();
        repository.delete(profile);
    }

    @Transactional(readOnly = true)
    public Page<ProfessionalProfile> searchProfessionals(String skill, String text, Pageable pageable) {

        Specification<ProfessionalProfile> spec =
                ProfessionalSpecification.isCompleted();

        if (skill != null && !skill.isBlank()) {
            spec = spec.and(ProfessionalSpecification.hasSkill(skill));
        }

        if (text != null && !text.isBlank()) {
            spec = spec.and(ProfessionalSpecification.textSearch(text));
        }

        return repository.findAll(spec, pageable);
    }

    @Transactional
    public void addSkill(AddSkillRequest request) {
        ProfessionalProfile profile = getCurrentProfessional();

        Skill skill = skillRepository.findById(request.skillId())
                .orElseThrow(() -> new NotFoundException("Skill not found"));

        boolean alreadyExists = profile.getSkills().stream()
                .anyMatch(ps -> ps.getSkill().getId().equals(skill.getId()));

        if (alreadyExists) {
            throw new SkillAlreadyAddedException("Skill already added");
        }

        ProfessionalSkill ps = new ProfessionalSkill();
        ps.setId(new ProfessionalSkillId(profile.getId(), skill.getId()));
        ps.setProfessionalProfile(profile);
        ps.setSkill(skill);

        profile.getSkills().add(ps);
        profile.setProfileCompleted(isProfileComplete(profile));
        repository.save(profile);
    }

    @Transactional
    public void removeSkill(UUID skillId) {

        ProfessionalProfile profile = getCurrentProfessional();

        boolean exists = professionalSkillRepository
                .existsByProfessionalProfileIdAndSkillId(profile.getId(), skillId);

        if (!exists) {
            throw new NotFoundException("Skill not found in profile");
        }

        professionalSkillRepository
                .deleteByProfessionalProfileIdAndSkillId(profile.getId(), skillId);

        profile.getSkills().removeIf(ps -> ps.getSkill().getId().equals(skillId));
        profile.setProfileCompleted(isProfileComplete(profile));
        repository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getMySkills() {

        ProfessionalProfile profile = getCurrentProfessional();

        return professionalSkillRepository
                .findByProfessionalProfileId(profile.getId())
                .stream()
                .map(ps -> skillMapper.toResponse(ps.getSkill()))
                .toList();
    }
}

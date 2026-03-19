package com.pronnect.professional.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.exception.NotFoundException;
import com.pronnect.exception.ProfileAlreadyExistsException;
import com.pronnect.professional.dto.CreateProfessionalProfileRequest;
import com.pronnect.professional.dto.UpdateProfessionalProfileRequest;
import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.professional.entity.ProfessionalSkill;
import com.pronnect.professional.entity.ProfessionalSkillId;
import com.pronnect.professional.repository.ProfessionalRepository;
import com.pronnect.professional.repository.ProfessionalSkillRepository;
import com.pronnect.skill.entity.Skill;
import com.pronnect.skill.repository.SkillRepository;
import com.pronnect.skill.dto.SkillResponse;
import com.pronnect.skill.mapper.SkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        ProfessionalProfile profile = ProfessionalProfile.builder()
                .id(UUID.randomUUID())
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

        profile.setHeadline(request.headline());
        profile.setDescription(request.description());
        profile.setContactEmail(request.contactEmail());

        return repository.save(profile);
    }

    @Transactional
    public void deleteProfile() {
        ProfessionalProfile profile = getCurrentProfessional();
        repository.delete(profile);
    }

    @Transactional(readOnly = true)
    public Page<ProfessionalProfile> searchProfessionals(String query, Pageable pageable) {

        if (query == null || query.isBlank()) {
            return repository.findByProfileCompletedTrue(pageable);
        }

        return repository.search(query.toLowerCase(), pageable);
    }

    @Transactional
    public void addSkill(UUID skillId) {

        ProfessionalProfile profile = getCurrentProfessional();

        if (professionalSkillRepository
                .existsByProfessionalProfileIdAndSkillId(profile.getId(), skillId)) {
            return;
        }

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new NotFoundException("Skill not found"));

        ProfessionalSkill ps = new ProfessionalSkill();

        ps.setId(new ProfessionalSkillId(profile.getId(), skillId));
        ps.setProfessionalProfile(profile);
        ps.setSkill(skill);

        professionalSkillRepository.save(ps);
    }

    @Transactional
    public void removeSkill(UUID skillId) {

        ProfessionalProfile profile = getCurrentProfessional();

        professionalSkillRepository
                .deleteByProfessionalProfileIdAndSkillId(profile.getId(), skillId);
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

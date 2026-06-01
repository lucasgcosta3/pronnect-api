package com.pronnect.company.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.company.entity.CompanyProfile;
import com.pronnect.company.dto.CreateCompanyProfileRequest;
import com.pronnect.company.dto.UpdateCompanyProfileRequest;
import com.pronnect.company.repository.CompanyRepository;
import com.pronnect.exception.NotFoundException;
import com.pronnect.exception.ProfileAlreadyExistsException;
import com.pronnect.storage.AvatarStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyRepository repository;
    private final AuthenticatedUserService authenticatedUserService;
    private final AvatarStorageService avatarStorageService;

    @Transactional
    public CompanyProfile createProfile(CreateCompanyProfileRequest request) {

        Account account = authenticatedUserService.getCurrentAccount();

        if (repository.existsByAccountId(account.getId())) {
            throw new ProfileAlreadyExistsException("Company profile already exists");
        }

        CompanyProfile profile = CompanyProfile.builder()
                .account(account)
                .name(request.name())
                .description(request.description())
                .contactEmail(request.contactEmail())
                .location(request.location())
                .avatarUrl(request.avatarUrl())
                .profileCompleted(false)
                .build();

        return repository.save(profile);
    }

    @Transactional(readOnly = true)
    public CompanyProfile getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company profile not found"));
    }

    @Transactional(readOnly = true)
    public CompanyProfile getCurrentCompany() {
        Account account = authenticatedUserService.getCurrentAccount();

        return repository.findByAccountId(account.getId())
                .orElseThrow(() -> new NotFoundException("Company profile not found"));
    }

    @Transactional
    public CompanyProfile updateProfile(UpdateCompanyProfileRequest request) {
        CompanyProfile profile = getCurrentCompany();

        profile.setName(request.name());
        profile.setDescription(request.description());
        profile.setContactEmail(request.contactEmail());
        profile.setLocation(request.location());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setProfileCompleted(isProfileComplete(profile));

        return repository.save(profile);
    }

    @Transactional
    public CompanyProfile updateAvatar(MultipartFile file) {
        CompanyProfile profile = getCurrentCompany();
        String avatarUrl = avatarStorageService.store(file);
        profile.setAvatarUrl(avatarUrl);
        return repository.save(profile);
    }

    private boolean isProfileComplete(CompanyProfile profile) {
        return profile.getName() != null && !profile.getName().isBlank()
                && profile.getDescription() != null && !profile.getDescription().isBlank()
                && profile.getContactEmail() != null && !profile.getContactEmail().isBlank()
                && profile.getLocation() != null && !profile.getLocation().isBlank();
    }

    @Transactional
    public void deleteProfile() {
        CompanyProfile profile = getCurrentCompany();
        repository.delete(profile);
    }

}
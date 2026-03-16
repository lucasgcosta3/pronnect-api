package com.pronnect.professional;

import com.pronnect.account.Account;
import com.pronnect.account.AccountRepository;
import com.pronnect.exception.NotFoundException;
import com.pronnect.professional.dto.CreateProfessionalProfileRequest;
import com.pronnect.professional.dto.ProfessionalProfileResponse;
import com.pronnect.professional.dto.UpdateProfessionalProfileRequest;
import com.pronnect.professional.mapper.ProfessionalProfileMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final ProfessionalProfileMapper mapper;

    @Transactional
    public ProfessionalProfileResponse createProfile(
            UUID accountId,
            CreateProfessionalProfileRequest request
    ) {

        if (profileRepository.existsByAccountId(accountId)) {
            throw new IllegalStateException("Professional profile already exists");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        ProfessionalProfile profile = mapper.toEntity(request);

        profile.setId(UUID.randomUUID());
        profile.setAccount(account);
        profile.setProfileCompleted(false);

        profileRepository.save(profile);

        return mapper.toResponse(profile);
    }

    @Transactional
    public Page<ProfessionalProfileResponse> searchProfessionals(Pageable pageable) {

        return profileRepository
                .findByProfileCompletedTrue(pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public ProfessionalProfileResponse updateProfile(
            UUID profileId,
            UpdateProfessionalProfileRequest request
    ) {

        ProfessionalProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException("Professional profile not found"));

        mapper.updateEntity(request, profile);

        profileRepository.save(profile);

        return mapper.toResponse(profile);
    }
}

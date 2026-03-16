package com.pronnect.company;

import com.pronnect.account.Account;
import com.pronnect.account.AccountRepository;
import com.pronnect.company.dto.CompanyProfileResponse;
import com.pronnect.company.dto.CreateCompanyProfileRequest;
import com.pronnect.company.dto.UpdateCompanyProfileRequest;
import com.pronnect.company.mapper.CompanyProfileMapper;
import com.pronnect.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;
    private final AccountRepository accountRepository;
    private final CompanyProfileMapper mapper;

    @Transactional
    public CompanyProfileResponse createProfile(
            UUID accountId,
            CreateCompanyProfileRequest request
    ) {

        if (companyProfileRepository.existsByAccountId(accountId)) {
            throw new IllegalStateException("Company profile already exists");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        CompanyProfile profile = mapper.toEntity(request);

        profile.setId(UUID.randomUUID());
        profile.setAccount(account);
        profile.setProfileCompleted(false);

        companyProfileRepository.save(profile);

        return mapper.toResponse(profile);
    }

    @Transactional
    public CompanyProfileResponse updateProfile(
            UUID profileId,
            UpdateCompanyProfileRequest request
    ) {

        CompanyProfile profile = companyProfileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException("Company profile not found"));

        mapper.updateEntity(request, profile);

        companyProfileRepository.save(profile);

        return mapper.toResponse(profile);
    }

}
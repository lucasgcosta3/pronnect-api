package com.pronnect.professional;

import com.pronnect.account.Account;
import com.pronnect.account.AccountRepository;
import com.pronnect.exception.NotFoundException;
import com.pronnect.professional.dto.ProfessionalProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalProfileRepository profileRepository;
    private final AccountRepository accountRepository;

    public ProfessionalProfile createProfile(UUID accountId, ProfessionalProfileRequest request) {

        if (profileRepository.existsByAccountId(accountId)) {
            throw new RuntimeException("Professional profile already exists");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        ProfessionalProfile profile = buildProfessional(account, request);

        return profileRepository.save(profile);
    }

    public Page<ProfessionalProfile> searchProfessionals(Pageable pageable) {
        return profileRepository.findByProfileCompletedTrue(pageable);
    }

    private ProfessionalProfile buildProfessional(Account account, ProfessionalProfileRequest request) {

        return ProfessionalProfile.builder()
                .id(UUID.randomUUID())
                .account(account)
                .headline(request.title())
                .description(request.description())
                .contactEmail(request.contactEmail())
                .profileCompleted(false)
                .build();
    }

}

package com.pronnect.professional.mapper;

import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.professional.dto.CreateProfessionalProfileRequest;
import com.pronnect.professional.dto.ProfessionalProfileResponse;
import com.pronnect.professional.dto.UpdateProfessionalProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfessionalMapper {

    ProfessionalProfile toEntity(CreateProfessionalProfileRequest request);

    ProfessionalProfileResponse toResponse(ProfessionalProfile entity);

    void updateEntity(
            UpdateProfessionalProfileRequest request,
            @MappingTarget ProfessionalProfile entity
    );

}

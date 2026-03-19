package com.pronnect.company.mapper;

import com.pronnect.company.entity.CompanyProfile;
import com.pronnect.company.dto.CompanyProfileResponse;
import com.pronnect.company.dto.CreateCompanyProfileRequest;
import com.pronnect.company.dto.UpdateCompanyProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyProfileMapper {

    CompanyProfile toEntity(CreateCompanyProfileRequest request);

    CompanyProfileResponse toResponse(CompanyProfile entity);

    void updateEntity(
            UpdateCompanyProfileRequest request,
            @MappingTarget CompanyProfile entity
    );

}

package com.pronnect.servicecontract.mapper;

import com.pronnect.servicecontract.dto.ServiceContractResponse;
import com.pronnect.servicecontract.entity.ServiceContract;
import org.springframework.stereotype.Component;

@Component
public class ServiceContractMapper {

    public ServiceContractResponse toResponse(ServiceContract contract) {
        return new ServiceContractResponse(
                contract.getId(),
                contract.getProposal().getId(),
                contract.getStatus().name(),
                contract.getStartedAt(),
                contract.getCompletedAt(),
                contract.getValidatedAt()
        );
    }
}
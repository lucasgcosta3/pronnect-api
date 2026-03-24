package com.pronnect.proposal.mapper;

import com.pronnect.proposal.dto.ProposalResponse;
import com.pronnect.proposal.entity.Proposal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProposalMapper {
    ProposalResponse toResponse(Proposal proposal);
}

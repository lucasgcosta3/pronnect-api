package com.pronnect.skill.mapper;

import com.pronnect.skill.entity.Skill;
import com.pronnect.skill.dto.SkillResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillResponse toResponse(Skill skill);
}

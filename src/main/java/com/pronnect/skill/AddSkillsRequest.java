package com.pronnect.skill;

import java.util.List;
import java.util.UUID;

public record AddSkillsRequest(
        List<UUID> skillIds
) {
}

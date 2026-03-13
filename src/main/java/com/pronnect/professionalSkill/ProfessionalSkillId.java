package com.pronnect.professionalSkill;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalSkillId implements Serializable {

    @Column(name = "professional_profile_id")
    private UUID professionalProfileId;

    @Column(name = "skill_id")
    private UUID skillId;

}

package com.pronnect.professional.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProfessionalSkillId implements Serializable {

    @Column(name = "professional_profile_id")
    private UUID professionalProfileId;

    @Column(name = "skill_id")
    private UUID skillId;

}

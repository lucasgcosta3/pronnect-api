package com.pronnect.professionalSkill;

import com.pronnect.professional.ProfessionalProfile;
import com.pronnect.skill.Skill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity
@Table(name = "professional_skill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalSkill {

    @EmbeddedId
    private ProfessionalSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("professionalProfileId")
    @JoinColumn(name = "professional_profile_id")
    private ProfessionalProfile professionalProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

}

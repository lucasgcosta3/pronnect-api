package com.pronnect.professional.spec;

import com.pronnect.professional.entity.ProfessionalProfile;
import com.pronnect.professional.entity.ProfessionalSkill;
import com.pronnect.skill.entity.Skill;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class ProfessionalSpecification {

    public static Specification<ProfessionalProfile> hasSkill(String skillName) {

        return (root, query, cb) -> {
            query.distinct(true);

            Join<ProfessionalProfile, ProfessionalSkill> skills = root.join("skills");
            Join<ProfessionalSkill, Skill> skill = skills.join("skill");

            return cb.equal(cb.lower(skill.get("name")), skillName.toLowerCase(Locale.ROOT));
        };
    }

    public static Specification<ProfessionalProfile> textSearch(String text) {

        return (root, query, cb) -> {

            String like = "%" + text.toLowerCase(Locale.ROOT) + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("headline")), like),
                    cb.like(cb.lower(root.get("description")), like)
            );
        };
    }

    public static Specification<ProfessionalProfile> isCompleted() {
        return (root, query, cb) ->
                cb.isTrue(root.get("profileCompleted"));
    }
}
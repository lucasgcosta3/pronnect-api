CREATE TABLE professional_skill (
    professional_profile_id UUID NOT NULL,
    skill_id UUID NOT NULL,

    PRIMARY KEY (professional_profile_id, skill_id),

    CONSTRAINT fk_professional_skill_profile
        FOREIGN KEY (professional_profile_id)
        REFERENCES professional_profile(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_professional_skill_skill
        FOREIGN KEY (skill_id)
        REFERENCES skill(id)
        ON DELETE CASCADE
);
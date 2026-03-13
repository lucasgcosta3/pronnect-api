CREATE TABLE social_link (
    id UUID PRIMARY KEY,
    professional_profile_id UUID NOT NULL,
    platform VARCHAR(50) NOT NULL,
    url TEXT NOT NULL,

    CONSTRAINT fk_social_profile
        FOREIGN KEY (professional_profile_id)
        REFERENCES professional_profile(id)
        ON DELETE CASCADE
);
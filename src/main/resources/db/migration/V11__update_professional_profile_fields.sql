ALTER TABLE professional_profile
RENAME COLUMN title TO headline;

CREATE INDEX idx_professional_headline
ON professional_profile(headline);
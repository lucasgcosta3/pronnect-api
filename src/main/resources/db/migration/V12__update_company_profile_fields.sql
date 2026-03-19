ALTER TABLE company_profile
RENAME COLUMN company_name TO name;

ALTER TABLE company_profile
ALTER COLUMN name TYPE VARCHAR(255);

ALTER TABLE company_profile
ADD COLUMN website VARCHAR(255);

ALTER TABLE company_profile
ADD COLUMN location VARCHAR(255);

ALTER TABLE company_profile
ADD COLUMN profile_completed BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE company_profile
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now();
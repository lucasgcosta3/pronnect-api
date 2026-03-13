CREATE TABLE company_profile (
    id UUID PRIMARY KEY,

    account_id UUID NOT NULL UNIQUE,

    name VARCHAR(255) NOT NULL,

    description TEXT,

    contact_email VARCHAR(255),

    website VARCHAR(255),

    location VARCHAR(255),

    profile_completed BOOLEAN NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_company_account
        FOREIGN KEY (account_id)
        REFERENCES account(id)
        ON DELETE CASCADE
);
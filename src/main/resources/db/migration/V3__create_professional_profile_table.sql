CREATE TABLE professional_profile (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL UNIQUE,
    title VARCHAR(150),
    description TEXT,
    contact_email VARCHAR(255),

    CONSTRAINT fk_professional_account
        FOREIGN KEY (account_id)
        REFERENCES account(id)
        ON DELETE CASCADE
);
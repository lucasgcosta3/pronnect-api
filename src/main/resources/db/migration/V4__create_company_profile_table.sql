CREATE TABLE company_profile (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL UNIQUE,
    company_name VARCHAR(200) NOT NULL,
    description TEXT,
    contact_email VARCHAR(255),

    CONSTRAINT fk_company_account
        FOREIGN KEY (account_id)
        REFERENCES account(id)
        ON DELETE CASCADE
);
CREATE TABLE proposal (
    id UUID PRIMARY KEY,

    company_id UUID NOT NULL,
    professional_id UUID NOT NULL,

    message TEXT,
    price NUMERIC(10,2),

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_proposal_company
        FOREIGN KEY (company_id)
        REFERENCES company_profile(id),

    CONSTRAINT fk_proposal_professional
        FOREIGN KEY (professional_id)
        REFERENCES professional_profile(id)
);
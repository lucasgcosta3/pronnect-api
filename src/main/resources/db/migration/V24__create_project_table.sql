CREATE TABLE project (
    id                UUID PRIMARY KEY,
    company_id        UUID NOT NULL REFERENCES company_profile(id),
    title             VARCHAR(255) NOT NULL,
    description       TEXT NOT NULL,
    payment_type      VARCHAR(20) NOT NULL,
    budget_min        NUMERIC(12,2),
    budget_max        NUMERIC(12,2),
    skills            TEXT,
    status            VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    ai_justification  TEXT,
    created_at        TIMESTAMP NOT NULL DEFAULT now()
);

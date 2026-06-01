CREATE TABLE project_bid (
    id                UUID PRIMARY KEY,
    project_id        UUID NOT NULL REFERENCES project(id),
    professional_id   UUID NOT NULL REFERENCES professional_profile(id),
    amount            NUMERIC(12,2) NOT NULL,
    delivery_days     INT NOT NULL,
    cover_letter      TEXT NOT NULL,
    portfolio_url     TEXT,
    status            VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at        TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE(project_id, professional_id)
);

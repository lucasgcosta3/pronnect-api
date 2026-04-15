CREATE TABLE service_contract (
    id UUID PRIMARY KEY,

    proposal_id UUID NOT NULL UNIQUE,

    status VARCHAR(30) NOT NULL,

    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    validated_at TIMESTAMP,

    CONSTRAINT fk_service_contract_proposal
        FOREIGN KEY (proposal_id)
        REFERENCES proposal(id)
);
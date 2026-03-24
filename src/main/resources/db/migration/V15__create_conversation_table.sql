CREATE TABLE conversation (
    id UUID PRIMARY KEY,

    proposal_id UUID UNIQUE NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_conversation_proposal
        FOREIGN KEY (proposal_id)
        REFERENCES proposal(id)
);
CREATE TABLE review (
    id UUID PRIMARY KEY,
    reviewer_account_id UUID NOT NULL,
    reviewed_account_id UUID NOT NULL,
    service_contract_id UUID NOT NULL,
    rating SMALLINT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_review_reviewer FOREIGN KEY (reviewer_account_id) REFERENCES account(id),
    CONSTRAINT fk_review_reviewed FOREIGN KEY (reviewed_account_id) REFERENCES account(id),
    CONSTRAINT fk_review_contract FOREIGN KEY (service_contract_id) REFERENCES service_contract(id),
    CONSTRAINT uq_review_unique UNIQUE (reviewer_account_id, reviewed_account_id, service_contract_id)
);

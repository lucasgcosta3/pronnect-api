CREATE TABLE payment (
    id UUID PRIMARY KEY,

    service_contract_id UUID NOT NULL UNIQUE,

    amount NUMERIC(10,2) NOT NULL,
    platform_fee NUMERIC(10,2),
    professional_amount NUMERIC(10,2),

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    released_at TIMESTAMP,

    CONSTRAINT fk_payment_service_contract
        FOREIGN KEY (service_contract_id)
        REFERENCES service_contract(id)
);
-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_bank_table.sql
CREATE TABLE banks
(
    id             UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    name           VARCHAR(255)   NOT NULL UNIQUE,
    account_number VARCHAR(255)   NOT NULL,
    total_amount   DECIMAL(19, 2) NOT NULL,
    created_by     UUID           NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_banks_created_by FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE NO ACTION
);

CREATE INDEX idx_banks_account_number ON banks (account_number);
CREATE INDEX idx_banks_name ON banks (name);

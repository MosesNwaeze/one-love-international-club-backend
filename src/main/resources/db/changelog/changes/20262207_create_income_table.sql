-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_income_table.sql
CREATE TABLE incomes
(
    id                         UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    type                       VARCHAR(255)   NOT NULL,
    amount                     DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    paid_by                    UUID           NOT NULL,
    bank_id                    UUID           NOT NULL,
    proof_of_payment           VARCHAR(255)   NOT NULL,
    proof_of_payment_public_id VARCHAR(222)   NOT NULL,
    created_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_incomes_paid_by FOREIGN KEY (paid_by) REFERENCES users (id) ON DELETE NO ACTION,
    CONSTRAINT fk_incomes_bank_id FOREIGN KEY (bank_id) REFERENCES banks (id) ON DELETE CASCADE

);

CREATE INDEX idx_incomes_paid_by ON incomes (paid_by);
CREATE INDEX idx_incomes_bank_id ON incomes (bank_id);

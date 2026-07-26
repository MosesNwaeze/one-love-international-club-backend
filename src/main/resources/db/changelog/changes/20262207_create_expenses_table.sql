-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_expenses_table.sql
CREATE TABLE expenses
(
    id                         UUID                    DEFAULT gen_random_uuid() PRIMARY KEY,
    type                       VARCHAR(255)   NOT NULL,
    amount                     DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    paid_by                    UUID           NOT NULL,
    user_id                    UUID           NOT NULL,
    proof_of_payment           VARCHAR(255)   NOT NULL,
    proof_of_payment_public_id VARCHAR(222)   NOT NULL,
    created_at                 TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_expenses_paid_by FOREIGN KEY (paid_by) REFERENCES users (id) ON DELETE NO ACTION,
    CONSTRAINT fk_expenses_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE

);

CREATE INDEX idx_expenses_paid_by ON expenses (paid_by);
CREATE INDEX idx_expenses_user_id ON expenses (user_id);

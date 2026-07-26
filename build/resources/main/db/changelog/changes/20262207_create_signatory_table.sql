-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_signatory_table.sql
CREATE TABLE signatories
(
    id         UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    signatory  UUID NOT NULL UNIQUE,
    created_by UUID NOT NULL,
    bank_id    UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_signatories_signatory FOREIGN KEY (signatory) REFERENCES Users (id) ON DELETE CASCADE,
    CONSTRAINT fk_signatories_created_by FOREIGN KEY (created_by) REFERENCES Users (id) ON DELETE CASCADE,
    CONSTRAINT fk_signatories_bank_d FOREIGN KEY (bank_id) REFERENCES banks (id) ON DELETE SET NULL
);

CREATE INDEX idx_signatories_signatory ON signatories (signatory);
CREATE INDEX idx_signatories_created_by ON signatories (created_by);
CREATE INDEX idx_signatories_bank_id ON signatories (bank_id);

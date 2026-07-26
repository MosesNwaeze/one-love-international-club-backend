-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_resignation_table.sql
CREATE TABLE resignations
(
    id                           UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id                      UUID NOT NULL UNIQUE,
    resignation_letter           VARCHAR(255),
    resignation_letter_public_id VARCHAR(255),
    reason_of_resignation        TEXT,
    resignation_status           VARCHAR(255),
    resignation_type             VARCHAR(255),
    benefit_id                   UUID,
    rejection_reason             TEXT,
    created_at                   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT resignations_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT resignations_benefit_id_fk FOREIGN KEY (benefit_id) REFERENCES benefits (id) ON DELETE SET NULL
);

CREATE INDEX idx_resignations_user_id ON resignations (user_id);
CREATE INDEX idx_resignations_benefit_id ON resignations (benefit_id);
CREATE INDEX idx_resignations_resignation_status ON resignations (resignation_status);
CREATE INDEX idx_resignations_resignation_type ON resignations (resignation_type);

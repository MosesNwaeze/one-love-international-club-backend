-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_benefits_table.sql
CREATE TABLE benefits
(
    id              UUID                    DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id         UUID           NOT NULL,
    amount_received DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    created_at      TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT benefits_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_benefits ON benefits (user_id);

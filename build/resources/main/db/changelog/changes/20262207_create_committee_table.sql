-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_committee_table.sql
CREATE TABLE committees
(
    id                    UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    name                  VARCHAR(255) NOT NULL UNIQUE,
    description           TEXT         NOT NULL,
    total_members_allowed INT          NOT NULL,
    resolution_report     TEXT         NOT NULL,
    amount_received       DECIMAL(19, 2),
    amount_spent          DECIMAL(19, 2),
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

CREATE INDEX idx_committees_name ON committees (name);
CREATE INDEX idx_committees_description ON committees (description);

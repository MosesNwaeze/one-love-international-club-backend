-- liquibase formatted sql
-- changeset moses.nwaeze:202613092026_create_notice_table.sql
CREATE TABLE notices
(
    id            UUID                  DEFAULT gen_random_uuid() PRIMARY KEY,
    notice_type   VARCHAR(255) NOT NULL,
    audience_type VARCHAR(255) NOT NULL,
    subject       VARCHAR(255) NOT NULL,
    message       TEXT         NOT NULL,
    save_as_draft Boolean      NOT NULL DEFAULT TRUE,
    created_at
                  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP             DEFAULT CURRENT_TIMESTAMP

);

CREATE INDEX idx_notices_type ON notices (notice_type);
CREATE INDEX idx_notices_audience_type ON notices (audience_type);
CREATE INDEX idx_notices_subject ON notices (subject);

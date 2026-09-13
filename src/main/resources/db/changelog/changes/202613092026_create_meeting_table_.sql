-- liquibase formatted sql
-- changeset moses.nwaeze:202613092026_create_meeting_table.sql
CREATE TABLE meetings
(
    id                 UUID                  DEFAULT gen_random_uuid() PRIMARY KEY,
    meeting_title      VARCHAR(255) NOT NULL UNIQUE,
    date               TIMESTAMP    NOT NULL,
    agender            TEXT         NOT NULL,
    venue              TEXT         NOT NULL,
    documents          VARCHAR(255),
    document_public_id VARCHAR(255),
    save_as_draft      Boolean      NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_meetings_meeting_title ON meetings (meeting_title);
CREATE INDEX idx_date_meeting_title ON meetings (date);
CREATE INDEX idx_venue_meeting_title ON meetings (venue);

-- liquibase formatted sql
-- changeset moses.nwaeze:202613092026_create_minutes_table.sql
CREATE TABLE minutes
(
    id                 UUID             DEFAULT gen_random_uuid() PRIMARY KEY,
    meeting_id         UUID    NOT NULL,
    discussion_summary TEXT    NOT NULL,
    resolution         TEXT    NOT NULL,
    save_as_draft      Boolean NOT NULL DEFAULT TRUE,
    created_at
                       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT minutes_meeting_id_fk FOREIGN KEY (meeting_id) REFERENCES meetings (id) ON DELETE CASCADE
);

CREATE INDEX idx_minutes_meeting_id ON minutes (meeting_id);

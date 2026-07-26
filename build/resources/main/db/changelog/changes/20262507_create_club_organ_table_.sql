-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_club_organ_table_.sql
CREATE TABLE club_organs
(
    id         UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_club_organs ON club_organs (name);

INSERT INTO club_organs (id, name)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Executive'),
       ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'General Assembly'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Board Of Trustee'),
       ('7c9e6679-7425-40de-944b-e07fc1f90ae7', 'Mayor'),
       ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Coordinators');
-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_role_table_.sql
CREATE TABLE roles
(
    id            UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    club_organ_id UUID         NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT roles_club_organ_id_fk FOREIGN KEY (club_organ_id) REFERENCES club_organs (id) ON DELETE CASCADE
);

CREATE INDEX idx_roles ON roles (name);

INSERT INTO roles (name, club_organ_id)
VALUES ('President', '550e8400-e29b-41d4-a716-446655440000'),
       ('Vice President', '550e8400-e29b-41d4-a716-446655440000'),
       ('Secretary', '550e8400-e29b-41d4-a716-446655440000'),
       ('Assistance Secretary', '550e8400-e29b-41d4-a716-446655440000'),
       ('Treasurer', '550e8400-e29b-41d4-a716-446655440000'),
       ('Chief Provost', '550e8400-e29b-41d4-a716-446655440000'),
       ('Financial Secretary', '550e8400-e29b-41d4-a716-446655440000'),
       ('Publicity Secretary', '550e8400-e29b-41d4-a716-446655440000'),
       ('Assistance Publicity Secretary', '550e8400-e29b-41d4-a716-446655440000'),
       ('Chief Welfare Officer', '550e8400-e29b-41d4-a716-446655440000'),
       ('Legal Adviser', '550e8400-e29b-41d4-a716-446655440000'),
       ('General Assembly', '6ba7b810-9dad-11d1-80b4-00c04fd430c8'),
       ('Board Of Trustee', '123e4567-e89b-12d3-a456-426614174000'),
       ('Mayor', '7c9e6679-7425-40de-944b-e07fc1f90ae7'),
       ('Coordinator', 'f47ac10b-58cc-4372-a567-0e02b2c3d479');
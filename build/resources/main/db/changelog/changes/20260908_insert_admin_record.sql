-- liquibase formatted sql
-- changeset moses.nwaeze:20260908_insert_admin_record.sql

INSERT INTO club_organs (id, name)
VALUES ('123e4567-e89b-12d3-a456-426614174009', 'System Admins');

INSERT INTO roles (name, club_organ_id)
VALUES ('Admin', '123e4567-e89b-12d3-a456-426614174009');
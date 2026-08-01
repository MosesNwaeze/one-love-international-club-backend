-- liquibase formatted sql
-- changeset moses.nwaeze:20260108_create_dues_table.sql
CREATE TABLE dues
(
    id          UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

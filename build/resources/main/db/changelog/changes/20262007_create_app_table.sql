-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_app_table.sql
CREATE TABLE apps
(
    id          UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    headquarter VARCHAR(255) NOT NULL,
    motto       VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_apps_name ON apps (name);
CREATE INDEX idx_apps_headquarters ON apps (headquarter);

INSERT INTO apps (name, headquarter, motto)
VALUES ('ONE LOVE INTERNATIONAL NOBLE CLUB', 'NIGERIA', 'ONE LOVE KEEP US TOGETHER.');
-- liquibase formatted sql
-- changeset moses.nwaeze:20262007_create_user_table_.sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    id                UUID         NOT NULL default gen_random_uuid() PRIMARY KEY,
    first_name        VARCHAR(255) NOT NULL,
    last_name         VARCHAR(255) NOT NULL,
    other_name        VARCHAR(255),
    user_type         VARCHAR(255) NOT NULL,
    title             VARCHAR(255),
    current_address   TEXT         NOT NULL,
    permanent_address TEXT,
    gender            TEXT         NOT NULL,
    marital_status    TEXT         NOT NULL,
    occupation        TEXT         NOT NULL,
    status            VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    phone_number      VARCHAR(255) NOT NULL UNIQUE,
    password          VARCHAR(255) NOT NULL,
    created_at        timestamp    NOT NULL default CURRENT_TIMESTAMP,
    updated_at        timestamp    NOT NULL default CURRENT_TIMESTAMP,
    is_root_admin     BOOLEAN               DEFAULT FALSE,
    date_of_birth     DATE         NOT NULL,
    profile_pic       VARCHAR(255),
    pic_public_id     VARCHAR(255)

)
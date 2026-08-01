-- liquibase formatted sql
-- changeset moses.nwaeze:20262007_create_user_table_.sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    id                              UUID         NOT NULL default gen_random_uuid() PRIMARY KEY,
    first_name                      VARCHAR(255) NOT NULL,
    last_name                       VARCHAR(255) NOT NULL,
    other_name                      VARCHAR(255),
    role_id                         UUID         NOT NULL,
    title                           VARCHAR(255),
    current_address                 TEXT         NOT NULL,
    permanent_address               TEXT,
    gender                          TEXT         NOT NULL,
    marital_status                  TEXT         NOT NULL,
    occupation                      TEXT         NOT NULL,
    status                          VARCHAR(255) NOT NULL,
    email                           VARCHAR(255) NOT NULL UNIQUE,
    phone_number                    VARCHAR(255) NOT NULL UNIQUE,
    password                        VARCHAR(255) NOT NULL,
    created_at                      timestamp    NOT NULL default CURRENT_TIMESTAMP,
    updated_at                      timestamp    NOT NULL default CURRENT_TIMESTAMP,
    is_root_admin                   BOOLEAN               DEFAULT FALSE,
    date_of_birth                   DATE         NOT NULL,
    profile_pic                     VARCHAR(255),
    pic_public_id                   VARCHAR(255),
    guarantor                       UUID,
    committee                       UUID,
    bank_account_name               VARCHAR(255),
    bank_account_number             VARCHAR(255),
    letter_of_undertaking           VARCHAR(255),
    letter_of_undertaking_public_id VARCHAR(255),
    registration_fee_url            VARCHAR(255),
    registration_fee_public_id      VARCHAR(255),
    registration_form_url           VARCHAR(255),
    registration_form_public_id     VARCHAR(255),
    approval_comment                VARCHAR(255),
    approval_status                VARCHAR(255) NOT NULL DEFAULT 'PENDING',

    CONSTRAINT users_guarantor_fk FOREIGN KEY (guarantor) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT users_committee_fk FOREIGN KEY (committee) REFERENCES committees (id) ON DELETE SET NULL,
    CONSTRAINT users_roles_fk FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE SET NULL
);

CREATE INDEX users_email_index ON users (email);
CREATE INDEX users_first_name_index ON users (first_name);
CREATE INDEX users_last_name_index ON users (last_name);
CREATE INDEX users_phone_number_index ON users (phone_number);
CREATE INDEX users_role_index ON users (role_id);
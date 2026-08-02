-- liquibase formatted sql
-- changeset moses.nwaeze:20260108_create_polls_table.sql
CREATE TABLE polls
(
    id         UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    question   VARCHAR(255) NOT NULL UNIQUE,
    options    JSONB        NOT NULL,
    votes      JSONB,
    close_date TIMESTAMP    NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID         NOT NULL,
    voted_by   JSONB,

    CONSTRAINT post_created_by FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE NO ACTION
);

CREATE INDEX polls_question_index ON polls (question);
CREATE INDEX polls_option_index ON polls USING GIN(options);
CREATE INDEX polls_votes_index ON polls USING GIN(votes);

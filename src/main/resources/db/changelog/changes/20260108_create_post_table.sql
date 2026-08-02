-- liquibase formatted sql
-- changeset moses.nwaeze:20260108_create_post_table.sql
CREATE TABLE posts
(
    id                   UUID                  DEFAULT gen_random_uuid() PRIMARY KEY,
    title                VARCHAR(255) NOT NULL UNIQUE,
    post_type            VARCHAR(255) NOT NULL,
    content              TEXT         NOT NULL,
    post_image           VARCHAR(255),
    post_image_public_id VARCHAR(255),
    total_viewed         INTEGER      NOT NULL DEFAULT 0,
    created_at           TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    created_by           UUID         NOT NULL,

    CONSTRAINT post_created_by FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE NO ACTION
);

CREATE INDEX posts_title_index ON posts (title);
CREATE INDEX posts_content_index ON posts (content);
CREATE INDEX posts_total_viewed_index ON posts (total_viewed);

-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_password_reset_table_.sql
CREATE TABLE password_reset_tokens
(
    id            BIGSERIAL PRIMARY KEY,
    token         VARCHAR(255) NOT NULL,
    expires_at    TIMESTAMP    NOT NULL,
    user_login_id UUID         NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_password_reset_tokens_token ON password_reset_tokens (token);
CREATE INDEX idx_password_reset_tokens_email ON password_reset_tokens (email);
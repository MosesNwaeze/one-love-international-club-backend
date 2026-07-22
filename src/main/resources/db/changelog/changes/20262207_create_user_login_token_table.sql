-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_user_login_2fa_token_table

CREATE TABLE user_login_tokens
(
    id            UUID PRIMARY KEY DEFAULT (gen_random_uuid()),
    user_login_id UUID  NOT NULL,
    access_key    VARCHAR(255),
    refresh_key   VARCHAR(255),
    token_type    VARCHAR(255),
    scope         VARCHAR(255),
    created_at    TIMESTAMP NOT NULL,
    expires_at    TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_login_tokens_user_login
        FOREIGN KEY (user_login_id)
            REFERENCES user_logins (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_user_login_tokens_user_login_id ON user_login_tokens (user_login_id);
CREATE INDEX idx_user_login_tokens_access_key ON user_login_tokens (access_key);
CREATE INDEX idx_user_login_tokens_refresh_key ON user_login_tokens (refresh_key);
CREATE INDEX idx_user_login_tokens_expires_at ON user_login_tokens (expires_at);
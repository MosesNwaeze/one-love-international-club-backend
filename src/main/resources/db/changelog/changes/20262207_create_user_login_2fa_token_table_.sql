-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_user_login_2fa_token_table

CREATE TABLE user_login_2fa_tokens
(
    id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID NOT NULL,
    token   UUID NOT NULL,
    created_at TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT user_login_2fa_tokens_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_user_login_2fa_tokens_user_id ON user_login_2fa_tokens (user_id);
CREATE INDEX idx_user_login_2fa_tokens_token ON user_login_2fa_tokens (token);

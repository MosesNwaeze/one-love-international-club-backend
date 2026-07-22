-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_user_login_2fa_token_table

CREATE TABLE user_login_2fa_tokens
(
    id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID NOT NULL,
    token   UUID NOT NULL
);

CREATE INDEX idx_user_login_2fa_tokens_user_id ON user_login_2fa_tokens (user_id);
CREATE INDEX idx_user_login_2fa_tokens_token ON user_login_2fa_tokens (token);
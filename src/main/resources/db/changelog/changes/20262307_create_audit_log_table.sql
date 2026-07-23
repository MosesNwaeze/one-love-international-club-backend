-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_audit_log_table

CREATE TABLE audit_log
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),

    user_id       UUID,

    username      VARCHAR(255) NOT NULL,

    action        VARCHAR(50)  NOT NULL,

    entity_type   VARCHAR(100) NOT NULL,

    entity_id     UUID,

    old_value     JSONB,

    new_value     JSONB,

    ip_address    VARCHAR(255),

    user_agent    TEXT,

    status        VARCHAR(255)  NOT NULL,

    error_message JSONB,

    operation     VARCHAR(255),

    created_by    UUID,

    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- liquibase formatted sql
-- changeset moses.nwaeze:202613092026_create_document_upload_table.sql
CREATE TABLE documents
(
    id             UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    file           VARCHAR(255) NOT NULL,
    file_public_id VARCHAR(255) NOT NULL,
    category       VARCHAR(255) NOT NULL,
    user_id        UUID         NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT document_uploaded_by_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_documents_user_id ON documents (user_id);

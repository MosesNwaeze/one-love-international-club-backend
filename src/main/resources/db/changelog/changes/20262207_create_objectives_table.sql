-- liquibase formatted sql
-- changeset moses.nwaeze:20262207_create_objectives_table_.sql
CREATE TABLE objectives
(
    id         UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    title      TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_objectives ON objectives (title);

INSERT INTO objectives (title)
VALUES ('To promote love, peace and unity among members.'),
       ('To bring together people of like minds under one umbrella for mutual assistance and exchange of ideas.'),
       ('To promote and protect the interest of members.'),
       ('To promote the welfare of members and to render assistance to members as stated in the constitution.'),
       ('To promote business development, innovation and maintain a forum for sharing of business ideas and modern business practices.'),
       ('To promote honor, dignity and self-respect among members and encourage their development of core values and character.'),
       ('To promote the social, economic and professional advancement of its members through organizing educative seminars for members.'),
       ('To promote peaceful coexistence with other Nigeria nationalities or foreigners.'),
       ('To promote and contribute positively towards the development of our native land its human resources through:
. Encouragement of and contribution of development project in the community.
.To promote love and unity and create an enabling environment for social-economic development.'),
       ('To recognize achievement of members of the club and celebrate their successes in their respective fields.');
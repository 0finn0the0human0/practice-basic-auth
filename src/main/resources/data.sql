/*
Author:     Benjamin Soto-Roberts
Desc:       Seed test data for practice basic auth authentication and authorization project.
Created:    5/18/2026
*/

INSERT INTO USERS (USERNAME, HASHED_PASSWORD)
    VALUES ('user1', '$2a$10$GisvYeNUuwRUiVb/Andi4uaqItdavpZAIkscPifY/MpPkRI7sw6uq'); -- secret

INSERT INTO USERS (USERNAME, HASHED_PASSWORD)
    VALUES ('admin1', '$2a$10$b1PPzL3SXdxA0sx3ecpwc.DxdCacIqxPoKsduTPkQogW.cDPcr8DS'); -- supersecret

INSERT INTO ROLES (ROLE_NAME, ROLE_DESC)
    VALUES ('USER', 'GENERAL USER...');

INSERT INTO ROLES (ROLE_NAME, ROLE_DESC)
    VALUES ('ADMIN',  'PRIVILEGED USER...');

INSERT INTO USER_ROLES (ROLE_NAME, USERNAME)
    VALUES ('USER', 'user1');

INSERT INTO USER_ROLES (ROLE_NAME, USERNAME)
    VALUES ('ADMIN', 'admin1');
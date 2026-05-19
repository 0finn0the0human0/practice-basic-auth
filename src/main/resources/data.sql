/*
Author:     Benjamin Soto-Roberts
Desc:       Seed test data for practice basic auth authentication and authorization project.
Created:    5/18/2026
*/

INSERT INTO USERS (USERNAME, HASHED_PASSWORD)
    VALUES ('user1', '$2a$10$kczb.mWtDQhQ9zcRRHb05OLi7xgDe1PmghW1RI5l6xct5RJ7kBkne');

INSERT INTO USERS (USERNAME, HASHED_PASSWORD)
    VALUES ('admin1', '$2a$10$pooCuO4T8pwVZDYb3oNzjul1z8TBcbiosJ9YmGY5JawhBxSUxx.MW');

INSERT INTO ROLES (ROLE_NAME, ROLE_DESC)
    VALUES ('USER', 'GENERAL USER...');

INSERT INTO ROLES (ROLE_NAME, ROLE_DESC)
    VALUES ('ADMIN',  'PRIVILEGED USER...');

INSERT INTO USER_ROLES (ROLE_NAME, USERNAME)
    VALUES ('USER', 'user1');

INSERT INTO USER_ROLES (ROLE_NAME, USERNAME)
    VALUES ('ADMIN', 'admin1');
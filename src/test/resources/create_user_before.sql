TRUNCATE usr RESTART IDENTITY CASCADE;

INSERT INTO usr (id, login, password, first_name, last_name, creation_date, state)
    VALUES (1, 'login', 'password', 'firstName', 'lastName', CURRENT_DATE, 'ACTIVE' );

INSERT INTO user_role (user_id, role)
    VALUES (1, 'ADMIN');

ALTER SEQUENCE seq_user RESTART WITH 2;


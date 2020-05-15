INSERT INTO usr (id, login, password, state, creation_date)
    VALUES (1, 'admin', '123', 'ACTIVE', CURRENT_DATE);

INSERT INTO usr (id, login, password, state, creation_date)
    VALUES (2, 'user', '123', 'ACTIVE', CURRENT_DATE);

INSERT INTO user_role (user_id, roles)
    VALUES (1, 'ADMIN'), (1, 'USER'), (2, 'USER');

ALTER SEQUENCE seq_user RESTART WITH 3;
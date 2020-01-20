insert into usr (id, login, password, state)
    values (1, 'admin', '123', 'ACTIVE');

insert into user_role (user_id, roles)
    values (1, 'ADMIN'), (1, 'USER');


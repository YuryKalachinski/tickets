insert into usr (id, login, password, state, creation_date)
    values (1, 'admin', '123', 'ACTIVE', CURRENT_DATE);

insert into usr (id, login, password, state, creation_date)
    values (2, 'user', '123', 'ACTIVE', CURRENT_DATE);

insert into user_role (user_id, roles)
    values (1, 'ADMIN'), (1, 'USER'), (2, 'USER');



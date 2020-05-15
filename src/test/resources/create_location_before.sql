TRUNCATE location RESTART IDENTITY CASCADE;

insert into location (id, name, number_of_place, number_of_row) values
(1, 'Minsk-Arena', 20, 20);

ALTER SEQUENCE seq_location RESTART WITH 2;
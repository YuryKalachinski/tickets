TRUNCATE usr RESTART IDENTITY CASCADE;
TRUNCATE location RESTART IDENTITY CASCADE;
TRUNCATE event RESTART IDENTITY CASCADE;
TRUNCATE ticket RESTART IDENTITY CASCADE;

ALTER SEQUENCE seq_user RESTART WITH 1;
ALTER SEQUENCE seq_event RESTART WITH 1;
ALTER SEQUENCE seq_location RESTART WITH 1;
ALTER SEQUENCE seq_ticket RESTART WITH 1;
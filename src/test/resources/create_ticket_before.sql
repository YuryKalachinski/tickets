TRUNCATE location RESTART IDENTITY CASCADE;

INSERT INTO location (id, name, number_of_place, number_of_row)
    VALUES (1, 'Minsk-Arena', 20, 10);

INSERT INTO event (id, name, date_time, location_id)
    VALUES (1, 'Bi-2', '2020-06-10 18:00', 1);

INSERT INTO ticket (id, place, row, ticket_status, event_id, location_id)
    VALUES (1, 1, 1, 'PURCHASED', 1, 1);

ALTER SEQUENCE seq_event RESTART WITH 2;
ALTER SEQUENCE seq_location RESTART WITH 2;
ALTER SEQUENCE seq_ticket RESTART WITH 2;
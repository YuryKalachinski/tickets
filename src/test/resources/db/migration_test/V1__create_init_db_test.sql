CREATE TABLE event (
    id int8 NOT NULL,
    date_time TIMESTAMP,
    name VARCHAR(255),
    location_id int8,
    PRIMARY KEY (id)
);

CREATE SEQUENCE seq_event START 1 INCREMENT 1 OWNED BY event.id;

CREATE TABLE location (
    id int8 NOT NULL,
    name VARCHAR(255),
    number_of_place int4,
    number_of_row int4,
    PRIMARY KEY (id)
);

CREATE SEQUENCE seq_location START 1 INCREMENT 1 OWNED BY location.id;

CREATE TABLE ticket (
    id int8 NOT NULL,
    place int4,
    row int4,
    ticket_status VARCHAR(255),
    event_id int8,
    location_id int8,
    user_id int8,
    PRIMARY KEY (id)
);

CREATE SEQUENCE seq_ticket START 1 INCREMENT 1 OWNED BY ticket.id;

CREATE TABLE user_role (
    user_id int8 NOT NULL,
    role VARCHAR(255)
);

CREATE TABLE usr (
    id int8 NOT NULL,
    creation_date TIMESTAMP,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    login VARCHAR(255),
    password VARCHAR(255),
    state VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE SEQUENCE seq_user START 1 INCREMENT 1 OWNED BY usr.id;

ALTER TABLE IF EXISTS event
    ADD CONSTRAINT location_to_event_fk
    FOREIGN KEY (location_id) REFERENCES location;

ALTER TABLE IF EXISTS ticket
    ADD CONSTRAINT event_to_ticket_fk
    FOREIGN KEY (event_id) REFERENCES event;

ALTER TABLE IF EXISTS ticket
    ADD CONSTRAINT location_to_ticket_fk
    FOREIGN KEY (location_id) REFERENCES location;

ALTER TABLE IF EXISTS ticket
    ADD CONSTRAINT user_to_ticket_fk
    FOREIGN KEY (user_id) REFERENCES usr;

ALTER TABLE IF EXISTS user_role
    ADD CONSTRAINT user_to_user_role_fk
    FOREIGN KEY (user_id) REFERENCES usr;
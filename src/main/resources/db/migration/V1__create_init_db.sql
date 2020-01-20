create sequence hibernate_sequence start 1 increment 1;
create table events (
    id int8 primary key,
    date_time timestamp,
    name varchar(255),
    location_id int8
);

create table events_tickets (
    event_id int8 not null,
    tickets_id int8 primary key
);

create table locations (
    id int8 primary key,
    name varchar(255),
    quantity_place int4,
    quantity_row int4,
    quantity_sector int4
);

create table locations_events (
    location_id int8 not null,
    events_id int8 primary key
);

create table locations_tickets (
    location_id int8 not null,
    tickets_id int8 primary key
);

create table tickets (
    id int8 not null,
    place int4,
    row int4,
    sector varchar(255),
    ticket_status varchar(255),
    event_id int8,
    location_id int8,
    primary key (id)
);

create table user_role (
    user_id int8 not null,
    roles varchar(255)
);

create table usr (
    id int8 not null,
    first_name varchar(255),
    last_name varchar(255),
    login varchar(255),
    password varchar(255),
    state varchar(255),
    primary key (id)
);

alter table if exists events
    add constraint events_to_locations_fk
    foreign key (location_id) references locations;

alter table if exists events_tickets
    add constraint events_tickets_to_tickets_fk
    foreign key (tickets_id) references tickets;

alter table if exists events_tickets
    add constraint events_tickets_to_events_fk
    foreign key (event_id) references events;

alter table if exists locations_events
    add constraint locations_events_to_events_fk
    foreign key (events_id) references events;

alter table if exists locations_events
    add constraint locations_events_to_locations_fk
    foreign key (location_id) references locations;

alter table if exists locations_tickets
    add constraint locations_tickets_to_tickets_fk
    foreign key (tickets_id) references tickets;

alter table if exists locations_tickets
    add constraint locations_tickets_to_locations_fk
    foreign key (location_id) references locations;

alter table if exists tickets
    add constraint tickets_to_events_fk
    foreign key (event_id) references events;

alter table if exists tickets
    add constraint tickets_to_locations_fk
    foreign key (location_id) references locations;

alter table if exists user_role
    add constraint user_role_to_usr_fk
    foreign key (user_id) references usr;
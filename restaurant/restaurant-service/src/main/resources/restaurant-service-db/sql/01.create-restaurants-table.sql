--liquibase formatted sql

create table restaurants
(
    id bigserial primary key,
    name varchar(100) not null,
    description text not null,
    rating numeric(2, 1) default 0.0 not null,
    preview_photo_url varchar,
    country varchar(100) not null,
    city varchar(100) not null,
    street varchar(150) not null,
    building varchar(20) not null,
    apartment varchar(20) not null,
    contact_phone varchar(20),
    working_hours varchar(100)
);

--rollback drop table restaurants;
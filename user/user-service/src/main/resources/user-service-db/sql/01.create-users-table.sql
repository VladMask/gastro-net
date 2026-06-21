--liquibase formatted sql

create table users (
    id bigserial primary key,
    firstname varchar(100) not null,
    lastname varchar(100) not null,
    email varchar(255) not null unique,
    phone_number varchar(32) not null,
    birth_date date not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

--rollback drop table users;
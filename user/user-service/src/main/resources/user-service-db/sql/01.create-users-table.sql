--liquibase formatted sql

create table users (
    id bigserial primary key,
    firstname varchar(20) not null,
    lastname varchar(20) not null,
    email varchar(60) not null unique,
    birth_date timestamp with time zone not null,
    password varchar(256) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

--rollback drop table users;
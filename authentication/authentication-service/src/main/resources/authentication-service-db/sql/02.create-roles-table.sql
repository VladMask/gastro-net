--liquibase formatted sql

create table roles (
    id smallserial primary key,
    name varchar(64) not null unique
);

--rollback drop table roles;
--liquibase formatted sql

create table addresses (
     id bigserial primary key,
     country varchar(100) not null,
     city varchar(100) not null,
     street varchar(150) not null,
     building varchar(20) not null,
     apartment varchar(20) not null
);

--rollback drop table addresses;
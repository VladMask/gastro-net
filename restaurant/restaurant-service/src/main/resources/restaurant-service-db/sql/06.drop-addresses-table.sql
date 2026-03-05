--liquibase formatted sql

drop table addresses;

-- rollback
-- create table addresses (
--     id bigserial primary key,
--     country varchar(100) not null,
--     city varchar(100) not null,
--     street varchar(150) not null,
--     building varchar(20) not null,
--     apartment varchar(20) not null
-- );
--
-- insert into addresses ( country, city, street, building, apartment)
-- select distinct country, city, street, building, apartment from restaurants_copy;
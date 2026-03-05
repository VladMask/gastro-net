--liquibase formatted sql

create table restaurants_copy (
    id bigserial primary key,
    name varchar(100) not null,
    description text not null,
    rating decimal(2,1) not null default 0.0,
    photos_url varchar not null,
    country varchar(100),
    city varchar(100) not null,
    street varchar(150) not null,
    building varchar(20) not null,
    apartment varchar(20) not null
);

-- rollback drop table restaurants_copy;
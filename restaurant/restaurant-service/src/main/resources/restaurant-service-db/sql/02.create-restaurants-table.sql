--liquibase formatted sql

create table restaurants (
    id bigserial primary key,
    name varchar(100) not null,
    description text not null,
    rating decimal(2,1) not null default 0.0,
    photos_url varchar not null,
    address_id bigint not null references addresses(id)
);

--rollback drop table restaurants;
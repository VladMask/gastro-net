--liquibase formatted sql

create table orders (
     id bigserial primary key,
     restaurant_id bigint not null,
     reservation_id bigint not null,
     created_at timestamp with time zone not null,
     status varchar not null
);

--rollback drop table orders;
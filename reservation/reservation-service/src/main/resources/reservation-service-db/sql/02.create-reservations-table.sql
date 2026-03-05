--liquibase formatted sql

create table reservations (
    id bigserial primary key,
    user_id bigint not null,
    restaurant_id bigint not null,
    reserved_at timestamp with time zone not null,
    reserved_until timestamp with time zone not null,
    guests_count smallint not null default 1,
    status varchar not null,
    created_at timestamp with time zone not null
);

--rollback drop table reservations;
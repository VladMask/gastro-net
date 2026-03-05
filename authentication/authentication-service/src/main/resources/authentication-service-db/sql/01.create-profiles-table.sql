--liquibase formatted sql

create table profiles (
    id bigserial primary key,
    user_id bigint not null,
    password_hash varchar(256) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

--rollback drop table profiles;
-- liquibase formatted sql

create table outbox_events (
    id bigserial primary key,
    header varchar not null,
    payload varchar not null,
    status varchar not null,
    retry smallint not null default 0,
    created_at timestamp
);

-- rollback drop table outbox_events;
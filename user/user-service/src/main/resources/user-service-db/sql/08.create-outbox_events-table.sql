--liquibase formatted sql

create table outbox_events (
    id bigserial primary key,
    header varchar not null,
    payload varchar not null,
    is_sent bool not null default false,
    created_at timestamp with time zone
);

-- rollback drop table outbox_events;
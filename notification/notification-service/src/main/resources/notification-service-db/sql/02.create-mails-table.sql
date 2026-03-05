-- liquibase formatted sql

create table mails (
    id bigserial primary key,
    receiver_email varchar not null,
    subject varchar not null,
    body varchar not null,
    status varchar not null,
    created_at timestamp with time zone not null
);

-- rollback drop table mails;


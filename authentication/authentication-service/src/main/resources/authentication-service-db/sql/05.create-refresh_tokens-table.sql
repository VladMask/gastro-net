--liquibase formatted sql

create table refresh_tokens(
    id bigserial primary key,
    profile_id bigint not null references profiles(id),
    token varchar not null,
    expiration_date timestamp not null
);

-- rollback drop table refresh_tokens;
--liquibase formatted sql

create table verification_codes (
     id bigserial primary key,
     receiver_email varchar not null,
     code_hash varchar not null,
     created_at timestamp not null,
     expires_at timestamp not null
);

--rollback drop table verification_codes;
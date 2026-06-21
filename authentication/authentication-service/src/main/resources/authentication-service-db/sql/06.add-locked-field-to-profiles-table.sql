--liquibase formatted sql

alter table profiles
    add column locked bool not null default false;

-- rollback alter table profiles
--     drop column locked;
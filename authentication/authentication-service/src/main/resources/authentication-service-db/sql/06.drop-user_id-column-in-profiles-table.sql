--liquibase formatted sql

alter table profiles
drop column user_id;

-- rollback alter table profiles
-- add column user_id bigint not null default 0;
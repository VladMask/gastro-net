-- liquibase formatted sql

alter table profiles
add column email varchar not null unique;

-- rollback alter table profiles drop column email;
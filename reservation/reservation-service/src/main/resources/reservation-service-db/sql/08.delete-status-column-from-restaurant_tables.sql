--liquibase formatted sql

alter table restaurant_tables
    drop column if exists status;

-- rollback alter table reservation_tables
--     add column status varchar not null default 'AVAILABLE';
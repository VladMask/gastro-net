--liquibase formatted sql

alter table users
alter column birth_date type date using (birth_date::date);

--rollback alter table users
--alter column birth_date type birth_date timestamp with time zone
--using (birth_date::timestamp with time zone);
--liquibase formatted sql

alter table users
drop column password;

--rollback alter table users
--add column password varchar(256) not null;
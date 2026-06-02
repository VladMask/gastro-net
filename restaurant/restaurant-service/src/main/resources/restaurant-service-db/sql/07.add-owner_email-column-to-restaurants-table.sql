--liquibase formatted sql

alter table restaurants
    add column owner_email varchar(255);

-- rollback alter table restaurants drop column owner_email;
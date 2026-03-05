--liquibase formatted sql

alter table restaurants_copy rename to restaurants;

-- rollback alter table restaurants rename to restaurants_copy;
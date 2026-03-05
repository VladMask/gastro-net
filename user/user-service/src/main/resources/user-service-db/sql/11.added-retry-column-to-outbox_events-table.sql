--liquibase formatted sql

alter table outbox_events
add column retry smallint not null default 0;

-- rollback
-- alter table outbox_events
--     drop column retry;
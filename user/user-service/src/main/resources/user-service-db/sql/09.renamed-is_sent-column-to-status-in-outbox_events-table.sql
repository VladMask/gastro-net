--liquibase formatted sql

alter table outbox_events
    rename is_sent to status;

-- rollback
-- alter table outbox_events
--     rename status to is_sent;

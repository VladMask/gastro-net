--liquibase formatted sql

alter table outbox_events
    alter column status drop default;
alter table outbox_events
    alter column status type varchar using (status::varchar);
alter table outbox_events
    alter column status set default 'NEW';
update outbox_events
    set status = 'NEW' where status = 'false';
update outbox_events
    set status = 'SUCCESS' where status = 'true';

-- rollback
-- update outbox_events
--     set status = 'false' where status = 'NEW';
-- update outbox_events
--     set status = 'true' where status = 'SUCCESS';
-- alter table outbox_events
--     alter column status drop default;
-- alter table outbox_events
--     alter column status type varchar using (status::bool);
-- alter table outbox_events
--     alter column status set default false;

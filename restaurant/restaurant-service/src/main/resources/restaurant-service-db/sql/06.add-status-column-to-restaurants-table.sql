--liquibase formatted sql

alter table restaurants
    add column status varchar(50) not null default 'PENDING_ACTIVATION';

update restaurants
set status = 'ACTIVE'
where status = 'PENDING_ACTIVATION';

-- rollback alter table restaurants drop column status;
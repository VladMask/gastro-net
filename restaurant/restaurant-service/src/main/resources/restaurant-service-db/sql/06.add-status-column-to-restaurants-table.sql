--liquibase formatted sql

alter table restaurants
    add column status varchar(50) not null default 'PENDING';

update restaurants
set status = 'ACTIVE'
where status = 'PENDING';

-- rollback alter table restaurants drop column status;
--liquibase formatted sql

create table banking_payments (
    id bigserial primary key,
    user_id bigint not null,
    restaurant_id bigint not null,
    amount decimal(14,2) not null,
    status varchar not null,
    created_at timestamp with time zone not null
);

--rollback drop table banking_payments;
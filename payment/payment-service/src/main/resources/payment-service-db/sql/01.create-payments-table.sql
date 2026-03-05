--liquibase formatted sql

create table payments (
     id bigserial primary key,
     order_id bigint not null,
     user_id bigint not null,
     restaurant_id bigint,
     amount decimal(14,2) not null,
     payment_method varchar not null,
     status varchar not null,
     created_at timestamp with time zone not null
);

--rollback drop table payments;
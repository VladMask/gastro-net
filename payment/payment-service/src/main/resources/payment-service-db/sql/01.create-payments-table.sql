--liquibase formatted sql

create table payments (
     id bigserial primary key,
     order_id bigint not null,
     user_id bigint not null,
     restaurant_id bigint,
     amount decimal(14,2) not null,
     payment_method varchar not null,
     status varchar not null,
     created_at timestamp not null,
     alfabank_order_id varchar(64),
     payment_form_url  varchar(512)
);

--rollback drop table payments;
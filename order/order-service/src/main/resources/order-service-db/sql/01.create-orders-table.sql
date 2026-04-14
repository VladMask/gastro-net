--liquibase formatted sql

create table orders (
     id bigserial primary key,
     restaurant_id bigint not null,
     reservation_id bigint,
     user_id bigint,
     total_price decimal(10,2),
     created_at timestamp not null,
     status varchar not null
);

--rollback drop table orders;
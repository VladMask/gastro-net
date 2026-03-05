--liquibase formatted sql

create table order_meals (
    id bigserial primary key,
    order_id bigint not null,
    meal_id bigint not null,
    quantity smallint not null default 1,
    price decimal(10,2) not null
);

--rollback drop table order_meals;
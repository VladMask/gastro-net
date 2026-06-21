--liquibase formatted sql

create table meal_categories(
    id bigserial primary key,
    name varchar not null,
    is_vegan boolean default false
);

-- rollback drop table meal_categories;
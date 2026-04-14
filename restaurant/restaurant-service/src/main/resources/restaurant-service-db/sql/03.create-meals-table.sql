--liquibase formatted sql

create table meals
(
    id bigserial primary key,
    name varchar not null,
    photo_url varchar,
    description text,
    price numeric(10, 2) not null,
    proteins smallint default 0 not null,
    fats smallint default 0 not null,
    carbs smallint default 0 not null,
    calories smallint default 0 not null,
    ingredients text,
    category_id bigint not null references meal_categories,
    restaurant_id bigint not null references restaurants
);

-- rollback drop table meals;
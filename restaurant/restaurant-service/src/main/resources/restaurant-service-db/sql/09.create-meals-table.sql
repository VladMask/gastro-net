--liquibase formatted sql

create table meals(
    id bigserial primary key,
    name varchar not null,
    photo_url varchar,
    description text,
    price decimal(10,2) not null,
    proteins smallint not null default 0,
    fats smallint not null default 0,
    carbs smallint not null default 0,
    calories smallint not null default 0,
    category_id bigint not null references meal_categories(id),
    restaurant_id bigint not null references restaurants(id)
);

-- rollback drop table meals;
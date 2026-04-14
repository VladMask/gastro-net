--liquibase formatted sql

create table restaurant_admins (
    id bigserial primary key,
    profile_id bigint not null,
    restaurant_id bigint not null references restaurants(id) on delete cascade,
    unique (profile_id, restaurant_id)
);

-- rollback drop table restaurant_admins;

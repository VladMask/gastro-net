--liquibase formatted sql

create table reviews (
    id bigserial primary key,
    restaurant_id bigint not null,
    user_id bigint not null,
    rating smallint not null check (rating between 1 and 5),
    content text,
    created_at timestamp not null
);

-- rollback drop table reviews;

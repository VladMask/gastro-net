--liquibase formatted sql

create table if not exists floor_schemas
(
    id bigserial primary key,
    restaurant_id bigint not null unique,
    elements text,
    canvas_width double precision,
    canvas_height double precision
);

-- rollback drop table floor_schemas;
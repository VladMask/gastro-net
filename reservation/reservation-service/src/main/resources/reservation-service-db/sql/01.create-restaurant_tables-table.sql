--liquibase formatted sql

create table restaurant_tables (
     id bigserial primary key,
     restaurant_id bigint not null,
     number varchar not null,
     capacity smallint not null default 1,
     location varchar not null,
     status varchar not null
);

--rollback drop table restaurant_tables;
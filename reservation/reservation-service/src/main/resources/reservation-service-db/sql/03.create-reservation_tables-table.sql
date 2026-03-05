--liquibase formatted sql

create table reservation_tables (
    reservation_id bigint not null references reservations(id),
    restaurant_table_id bigint not null references restaurant_tables(id),
    primary key (reservation_id, restaurant_table_id)
);

-- rollback drop table reservation_tables;
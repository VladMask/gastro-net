-- liquibase formatted sql

alter table reservation_tables
    drop constraint if exists fk_reservation_tables_restaurant_table_id;

alter table reservation_tables
    add constraint fk_reservation_tables_restaurant_table_id
        foreign key (restaurant_table_id)
            references restaurant_tables(id)
            on delete cascade;

--rollback alter table reservation_tables
-- drop constraint if exists fk_reservation_tables_restaurant_table_id;
-- alter table reservation_tables
-- add constraint fk_reservation_tables_restaurant_table_id
-- foreign key (restaurant_table_id) references restaurant_tables(id);
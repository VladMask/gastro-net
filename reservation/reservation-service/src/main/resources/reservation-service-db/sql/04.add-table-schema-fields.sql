-- liquibase formatted sql

alter table restaurant_tables
    add column if not exists pos_x   double precision default 0,
    add column if not exists pos_y   double precision default 0,
    add column if not exists width   double precision default 10,
    add column if not exists height  double precision default 10,
    add column if not exists shape   varchar(20)      default 'rect',
    add column if not exists label   varchar(50);

-- rollback
-- alter table restaurant_tables
--     drop column if exists pos_x,
--     drop column if exists pos_y,
--     drop column if exists width,
--     drop column if exists height,
--     drop column if exists shape,
--     drop column if exists label;
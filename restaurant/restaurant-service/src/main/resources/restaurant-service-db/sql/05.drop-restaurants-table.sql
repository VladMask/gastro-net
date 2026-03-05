--liquibase formatted sql

drop table restaurants;

-- rollback
-- create table restaurants (
--     id bigserial primary key,
--     name varchar(100) not null,
--     description text not null,
--     rating decimal(2,1) not null default 0.0,
--     photos_url varchar not null,
--     address_id bigint not null references addresses(id)
-- );
--
-- insert into restaurants (name, description, rating, photos_url, address_id)
-- select distinct rc.name, rc.description, rc.rating, rc.photos_url, a.id from addresses a
-- join restaurants_copy rc
-- on a.country = rc.country
-- and a.city = rc.city
-- and a.street = rc.street
-- and a.building = rc.building
-- and a.apartment = rc.apartment;
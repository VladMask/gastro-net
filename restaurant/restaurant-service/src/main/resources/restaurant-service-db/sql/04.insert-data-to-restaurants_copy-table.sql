--liquibase formatted sql

insert into restaurants_copy (
                              name,
                              description,
                              rating,
                              photos_url,
                              country,
                              city,
                              street,
                              building,
                              apartment
)
select r.name, r.description, r.rating, r.photos_url,
       a.country, a.city, a.street, a.building, a.apartment
from addresses a join restaurants r on r.address_id = a.id;

-- rollback truncate table restaurants_copy;
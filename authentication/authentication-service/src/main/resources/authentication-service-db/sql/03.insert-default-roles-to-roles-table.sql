-- liquibase formatted sql

insert into roles (name)
values
('USER'),
('RESTAURANT_ADMIN'),
('PLATFORM_ADMIN');

-- rollback delete from roles r where r.name = 'USER';
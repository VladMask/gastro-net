-- liquibase formatted sql

insert into roles(name) values ('USER');

-- rollback delete from roles r where r.name = 'USER';
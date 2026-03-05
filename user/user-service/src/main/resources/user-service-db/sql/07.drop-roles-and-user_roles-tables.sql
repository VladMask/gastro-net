--liquibase formatted sql

drop table user_roles;
drop table roles;

-- rollback
-- create table roles (
--     id smallserial primary key,
--     name varchar(20) not null
-- );
-- create table user_roles (
--     user_id bigint references users(id),
--     role_id smallint references roles(id),
--     primary key (user_id, role_id)
-- );
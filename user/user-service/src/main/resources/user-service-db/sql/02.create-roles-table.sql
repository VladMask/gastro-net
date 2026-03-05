--liquibase formatted sql

create table roles (
  id smallserial primary key,
  name varchar(20) not null
);

--rollback drop table roles;
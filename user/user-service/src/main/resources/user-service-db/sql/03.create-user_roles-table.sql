--liquibase formatted sql

create table user_roles (
  user_id bigint references users(id),
  role_id smallint references roles(id),
  primary key (user_id, role_id)
);

--rollback drop table user_roles;
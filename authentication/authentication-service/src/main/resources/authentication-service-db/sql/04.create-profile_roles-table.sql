--liquibase formatted sql

create table profile_roles (
  profile_id bigint references profiles(id),
  role_id smallint references roles(id),
  primary key (profile_id, role_id)
);

--rollback drop table profile_roles;
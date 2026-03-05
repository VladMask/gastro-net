--liquibase formatted sql

create unique index uq_profiles_user_id_idx on profiles(user_id);

--rollback drop index uq_profiles_user_id_idx;
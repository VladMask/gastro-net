create table if not exists notifications (
    id         bigserial    primary key,
    user_id    bigint       not null,
    title      varchar(255) not null,
    body       text         not null,
    type       varchar(64),
    read       boolean      not null default false,
    created_at timestamptz  not null default now()
);

create index if not exists idx_notifications_user_id on notifications(user_id);

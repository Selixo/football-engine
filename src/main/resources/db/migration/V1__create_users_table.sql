create table users
(
    id            uuid         not null,
    email         varchar(254) not null,
    password_hash varchar(100) not null,
    first_name    varchar(100) not null,
    last_name     varchar(100) not null,
    role          varchar(32)  not null,
    created_at    timestamptz  not null,
    updated_at    timestamptz  not null,
    constraint pk_users primary key (id),
    constraint uk_users_email unique (email),
    constraint ck_users_role check (role in ('COACH', 'ASSISTANT_COACH', 'ADMIN'))
);

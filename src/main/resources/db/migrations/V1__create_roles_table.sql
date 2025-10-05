create table roles (
    id serial primary key,
    name varchar(50) not null unique,
    created_at timestamp default now()
)
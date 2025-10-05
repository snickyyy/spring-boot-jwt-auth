create table users (
    id serial primary key,
    email varchar(100) not null unique,
    password varchar not null,
    role_id int references roles(id) on delete set null,
    created_at timestamp default now()
)

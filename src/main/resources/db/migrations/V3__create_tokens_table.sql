create table tokens(
    id uuid primary key,
    user_id int references users(id) on delete cascade,
    exp timestamp not null,
    created_at timestamp default now()
)
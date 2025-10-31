ALTER TABLE users DROP COLUMN role_id;
CREATE TABLE users_roles (
    user_id int NOT NULL references users(id),
    role_id int NOT NULL references roles(id),
    PRIMARY KEY (user_id, role_id)
);
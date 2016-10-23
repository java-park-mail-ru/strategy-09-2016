create table users(
    email text not null,
    id bigserial primary key,
    login text not null,
    password text not null,
    rating INTEGER not null
);


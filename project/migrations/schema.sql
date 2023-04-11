--liquibase formatted sql

--changeset ZhenyaDudko:1
create table if not exists chat
(
    id bigint primary key
);

create table if not exists link
(
    id bigint primary key generated always as identity,
    chat_id bigint references chat (id) on delete cascade,
    url text not null,
    last_update date not null default now()
);

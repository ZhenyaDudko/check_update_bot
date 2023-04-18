--liquibase formatted sql

--changeset ZhenyaDudko:1
create table if not exists chat
(
    id bigint primary key
);

create table if not exists link
(
    id bigint primary key generated always as identity,
    url text not null unique,
    last_update timestamp not null default now(),
    comment_count int default null,
    answer_count int default null
);

create table if not exists chat_link
(
    chat_id bigint references chat (id) on delete cascade,
    link_id bigint references link (id) on delete cascade,
    primary key(chat_id, link_id)
);

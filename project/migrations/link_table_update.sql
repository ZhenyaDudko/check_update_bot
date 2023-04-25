--liquibase formatted sql

--changeset ZhenyaDudko:2
alter table link
add column if not exists answer_count int default null,
add column if not exists comment_count int default null;

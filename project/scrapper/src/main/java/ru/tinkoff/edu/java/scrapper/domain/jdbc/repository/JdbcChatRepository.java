package ru.tinkoff.edu.java.scrapper.domain.jdbc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatRepository;

@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_CHAT_QUERY = "insert into chat (id) values (?);";

    private static final String DELETE_CHAT_QUERY = "delete from chat where id = ?;";

    private static final String COUNT_CHAT_BY_ID_QUERY = "select count(*) from chat where id = ?;";

    @Override
    public void add(long chatId) {
        jdbcTemplate.update(INSERT_CHAT_QUERY, chatId);
    }

    @Override
    public void remove(long chatId) {
        jdbcTemplate.update(DELETE_CHAT_QUERY, chatId);
    }

    @Override
    public Long countChatById(long chatId) {
        return jdbcTemplate.queryForObject(COUNT_CHAT_BY_ID_QUERY, Long.class, chatId);
    }
}

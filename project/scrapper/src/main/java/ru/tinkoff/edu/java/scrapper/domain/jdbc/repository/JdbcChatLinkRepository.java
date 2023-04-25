package ru.tinkoff.edu.java.scrapper.domain.jdbc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.ChatRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ChatRowMapper chatRowMapper = new ChatRowMapper();

    private static final String INSERT_CHAT_LINK_QUERY =
            "insert into chat_link (chat_id, link_id) values (?, ?);";

    private static final String DELETE_CHAT_LINK_QUERY =
            "delete from chat_link where chat_id = ? and link_id = ?;";

    private static final String COUNT_CHAT_LINK_BY_LINK_ID_QUERY =
            "select count(*) from chat_link where link_id = ?;";

    private static final String SELECT_CHATS_BY_LINK_QUERY =
            "select chat_id as id from chat_link where link_id = ?;";

    @Override
    public void addChatLink(long chatId, long linkId) {
        jdbcTemplate.update(INSERT_CHAT_LINK_QUERY, chatId, linkId);
    }

    @Override
    public void removeChatLink(long chatId, long linkId) {
        jdbcTemplate.update(DELETE_CHAT_LINK_QUERY, chatId, linkId);
    }

    @Override
    public Long countChatByLinkId(long linkId) {
        return jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_LINK_ID_QUERY, Long.class, linkId);
    }

    @Override
    public List<Chat> getChatsByLinkId(long id) {
        return jdbcTemplate.query(SELECT_CHATS_BY_LINK_QUERY, chatRowMapper, id);
    }
}

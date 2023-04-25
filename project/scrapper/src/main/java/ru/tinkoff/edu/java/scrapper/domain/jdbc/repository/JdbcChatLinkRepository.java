package ru.tinkoff.edu.java.scrapper.domain.jdbc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;

import java.net.URI;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_CHAT_LINK_GETTING_ID_FROM_LINK_QUERY = """
            insert into chat_link (chat_id, link_id)
            values (?, (select id from link where url = ?));
            """;

    private static final String DELETE_CHAT_LINK_GETTING_ID_FROM_LINK_QUERY = """
            delete from chat_link where chat_id = ? and link_id in
            (select id from link where url = ?);
            """;

    @Override
    public void addChatLinkByUrl(long chatId, URI url) {
        jdbcTemplate.update(INSERT_CHAT_LINK_GETTING_ID_FROM_LINK_QUERY, chatId, url.toString());
    }

    @Override
    public void removeChatLinkByUrl(long chatId, URI url) {
        jdbcTemplate.update(DELETE_CHAT_LINK_GETTING_ID_FROM_LINK_QUERY, chatId, url.toString());
    }
}

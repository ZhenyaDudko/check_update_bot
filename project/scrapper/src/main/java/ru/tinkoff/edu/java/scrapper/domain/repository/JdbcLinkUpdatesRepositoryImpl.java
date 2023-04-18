package ru.tinkoff.edu.java.scrapper.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.ChatRowMapper;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRowMapper;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class JdbcLinkUpdatesRepositoryImpl implements JdbcLinkUpdatesRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper linkRowMapper;
    private final ChatRowMapper chatRowMapper;

    public JdbcLinkUpdatesRepositoryImpl(JdbcTemplate jdbcTemplate, LinkRowMapper linkRowMapper, ChatRowMapper chatRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.linkRowMapper = linkRowMapper;
        this.chatRowMapper = chatRowMapper;
    }

    @Override
    public void updateTimeByLinkId(long id, OffsetDateTime time) {
        jdbcTemplate.update("update link set last_update = ? where id = ?", time, id);
    }

    @Override
    public List<Link> getLongAgoUpdated() {
        return jdbcTemplate.query("select * from link where last_update < now() - interval '1 hour';", linkRowMapper);
    }

    @Override
    public List<Chat> getChatsByLinkId(long id) {
        return jdbcTemplate.query("select chat.id from chat join chat_link on chat.id = chat_link.chat_id " +
                "where link_id = ?;", chatRowMapper, id);
    }

}

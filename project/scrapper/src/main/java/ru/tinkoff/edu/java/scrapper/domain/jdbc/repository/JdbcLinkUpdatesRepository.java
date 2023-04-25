package ru.tinkoff.edu.java.scrapper.domain.jdbc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkUpdatesRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.ChatRowMapper;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRowMapper;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkUpdatesRepository implements LinkUpdatesRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper linkRowMapper = new LinkRowMapper();
    private final ChatRowMapper chatRowMapper = new ChatRowMapper();

    private static final String CHANGE_LINK_LAST_UPDATE_TIME_QUERY =
            "update link set last_update = ? where id = ?";

    private static final String SELECT_LONG_AGO_UPDATED_LINKS_QUERY =
            "select * from link where last_update < now() - interval '1 hour';";

    private static final String SELECT_CHATS_BY_LINK_QUERY =
            "select chat_id as id from chat_link where link_id = ?;";

    @Override
    public void updateTimeByLinkId(long id, OffsetDateTime time) {
        jdbcTemplate.update(CHANGE_LINK_LAST_UPDATE_TIME_QUERY, time, id);
    }

    @Override
    public List<Link> getLongAgoUpdated() {
        return jdbcTemplate.query(SELECT_LONG_AGO_UPDATED_LINKS_QUERY, linkRowMapper);
    }

    @Override
    public List<Chat> getChatsByLinkId(long id) {
        return jdbcTemplate.query(SELECT_CHATS_BY_LINK_QUERY, chatRowMapper, id);
    }

}

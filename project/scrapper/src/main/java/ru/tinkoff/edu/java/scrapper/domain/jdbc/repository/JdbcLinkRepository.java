package ru.tinkoff.edu.java.scrapper.domain.jdbc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRowMapper;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper linkRowMapper = new LinkRowMapper();

    private static final String SELECT_ID_FROM_LINK_BY_URL_QUERY =
            "select id from link where url = ?";

    private static final String INSERT_LINK_QUERY = """
            insert into link (url, answer_count, comment_count) values (?, ?, ?)
            returning id, url, last_update, answer_count, comment_count;
            """;

    private static final String DELETE_LINK_QUERY =
            "delete from link where id = ?;";

    private static final String SELECT_ALL_FROM_LINK_QUERY =
            "select * from link where url = ?;";

    private static final String SELECT_ALL_LINKS_BY_CHAT_ID_QUERY = """
            select link.id as id, link.url as url, link.last_update as last_update,
            link.answer_count as answer_count, link.comment_count as comment_count
            from chat_link cl join link on cl.link_id = link.id where cl.chat_id = ?;
            """;

    private static final String CHANGE_LINK_LAST_UPDATE_TIME_QUERY =
            "update link set last_update = ? where id = ?";

    private static final String SELECT_LONG_AGO_UPDATED_LINKS_QUERY =
            "select * from link where last_update < now() - interval '1 hour';";

    @Override
    public Link addLink(long chatId, URI url) {
        return addLink(chatId, url, null, null);
    }

    @Override
    public Link addLink(long chatId, URI url, Integer answerCount, Integer commentCount) {
        String urlString = url.toString();
        return jdbcTemplate.queryForObject(INSERT_LINK_QUERY, linkRowMapper,
                urlString, answerCount, commentCount);
    }

    @Override
    public void removeLink(long chatId, long id) {
        jdbcTemplate.update(DELETE_LINK_QUERY, id);
    }

    @Override
    public Long getIdByUrl(URI url) {
        return jdbcTemplate.queryForObject(SELECT_ID_FROM_LINK_BY_URL_QUERY, Long.class, url.toString());
    }

    @Override
    public List<Link> getLinksByUrl(URI url) {
        return jdbcTemplate.query(SELECT_ALL_FROM_LINK_QUERY, linkRowMapper, url.toString());
    }

    @Override
    public List<Link> getAll(long chatId) {
        return jdbcTemplate.query(SELECT_ALL_LINKS_BY_CHAT_ID_QUERY, linkRowMapper, chatId);
    }

    @Override
    public void updateTimeByLinkId(long id, OffsetDateTime time) {
        jdbcTemplate.update(CHANGE_LINK_LAST_UPDATE_TIME_QUERY, time, id);
    }

    @Override
    public List<Link> getLongAgoUpdated() {
        return jdbcTemplate.query(SELECT_LONG_AGO_UPDATED_LINKS_QUERY, linkRowMapper);
    }
}

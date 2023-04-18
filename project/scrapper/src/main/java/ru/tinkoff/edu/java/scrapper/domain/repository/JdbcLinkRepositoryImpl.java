package ru.tinkoff.edu.java.scrapper.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRowMapper;

import java.net.URI;
import java.util.List;

@Repository
public class JdbcLinkRepositoryImpl implements JdbcLinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper linkRowMapper;

    public JdbcLinkRepositoryImpl(JdbcTemplate jdbcTemplate, LinkRowMapper linkRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.linkRowMapper = linkRowMapper;
    }

    @Override
    @Transactional
    public Link add(long chatId, URI url) {
        String urlString = url.toString();
        jdbcTemplate.update("insert into link (url) select ? " +
                "where not exists (select from link where url = ?);", urlString, urlString);
        jdbcTemplate.update("insert into chat_link (chat_id, link_id) " +
                "values (?, (select id from link where url = ?));", chatId, urlString);
        List<Link> links = jdbcTemplate.query("select * from link where url = ?", linkRowMapper, urlString);
        return links.get(0);
    }

    @Override
    @Transactional
    public Link remove(long chatId, URI url) throws LinkNotFoundException {
        String urlString = url.toString();
        List<Link> links = jdbcTemplate.query("select * from link where url = ?", linkRowMapper, urlString);
        if (links.size() == 0) {
            throw new LinkNotFoundException("Link not found: " + urlString);
        }
        jdbcTemplate.update("delete from chat_link where chat_id = ? and link_id in " +
                "(select id from link where url = ?);", chatId, urlString);
        jdbcTemplate.update("with count_refs as (select count(*) cnt from chat_link join link " +
                "on chat_link.link_id=link.id where link.url = ?) " +
                "delete from link where (select cnt from count_refs) = 0 and url = ?;", urlString, urlString);
        return links.get(0);
    }

    @Override
    public List<Link> getAll(long chatId) {
        return jdbcTemplate.query("select link.id as id, link.url as url, link.last_update as last_update " +
                "from chat_link cl join link on cl.link_id = link.id " +
                "where cl.chat_id = ?;", linkRowMapper, chatId);
    }
}

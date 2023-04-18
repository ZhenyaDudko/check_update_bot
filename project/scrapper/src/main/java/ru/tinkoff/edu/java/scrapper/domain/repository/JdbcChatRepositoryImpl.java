package ru.tinkoff.edu.java.scrapper.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepositoryImpl implements JdbcChatRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcLinkRepository linkRepository;

    @Override
    public void add(long chatId) {
        jdbcTemplate.update("insert into chat (id) values (?);", chatId);
    }

    @Override
    @Transactional
    public void remove(long chatId) throws ChatNotFoundException {
        Long countChat = jdbcTemplate.queryForObject("select count(*) from chat where id = ?;", Long.class, chatId);
        if (countChat == null || countChat == 0) {
            throw new ChatNotFoundException("Chat not found: " + chatId);
        }
        List<Link> linkList = linkRepository.getAll(chatId);
        for (Link link : linkList) {
            try {
                linkRepository.remove(chatId, link.getUrl());
            } catch (LinkNotFoundException ignored) {}
        }
        jdbcTemplate.update("delete from chat where id = ?;", chatId);
    }
}

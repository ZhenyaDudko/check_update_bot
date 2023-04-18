package ru.tinkoff.edu.java.scrapper.domain.repository;

import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.net.URI;
import java.util.List;

public interface JdbcLinkRepository {

    Link add(long chatId, URI url);
    Link addAnswerComment(long chatId, URI url, Long answerCount, Long commentCount);
    Link remove(long chatId, URI url) throws LinkNotFoundException;
    List<Link> getAll(long chatId);
}

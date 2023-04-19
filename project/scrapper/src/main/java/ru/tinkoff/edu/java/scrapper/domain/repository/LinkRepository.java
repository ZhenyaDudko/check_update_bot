package ru.tinkoff.edu.java.scrapper.domain.repository;

import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.net.URI;
import java.util.List;

public interface LinkRepository {

    void add(long chatId, URI url);
    void addAnswerComment(long chatId, URI url, Integer answerCount, Integer commentCount);
    void remove(long chatId, URI url) throws LinkNotFoundException;
    List<Link> getLinksByUrl(URI url);
    List<Link> getAll(long chatId);
}

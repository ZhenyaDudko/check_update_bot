package ru.tinkoff.edu.java.scrapper.domain.service;

import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.net.URI;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, URI url);
    Link remove(long tgChatId, URI url) throws LinkNotFoundException;
    List<Link> listAll(long tgChatId);
}

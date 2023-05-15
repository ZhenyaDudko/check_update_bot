package ru.tinkoff.edu.java.scrapper.domain.service;

import java.net.URI;
import java.util.List;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

public interface LinkService {
    Link add(long tgChatId, URI url);

    Link remove(long tgChatId, URI url) throws LinkNotFoundException;

    List<Link> listAll(long tgChatId);
}

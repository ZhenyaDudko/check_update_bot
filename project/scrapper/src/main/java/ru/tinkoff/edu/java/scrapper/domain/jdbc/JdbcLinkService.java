package ru.tinkoff.edu.java.scrapper.domain.jdbc;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkService;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.net.URI;
import java.util.List;

@Component
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository linkRepository;

    public JdbcLinkService(JdbcLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Link add(long chatId, URI url) {
        return linkRepository.add(chatId, url);
    }

    @Override
    public Link remove(long chatId, URI url) throws LinkNotFoundException {
        return linkRepository.remove(chatId, url);
    }

    @Override
    public List<Link> listAll(long chatId) {
        return linkRepository.getAll(chatId);
    }
}

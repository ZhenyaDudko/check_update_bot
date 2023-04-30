package ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import parser.Parser;
import results.ParsingResult;
import results.StackOverflowParsingResult;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkService;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final StackOverflowClient stackOverflowClient;
    private final Parser parser = new Parser();

    @Override
    @Transactional
    public Link add(long chatId, URI url) {
        ParsingResult parsingResult = parser.parse(url.toString());
        if (parsingResult == null) {
            throw new IllegalArgumentException("Not supported link");
        }
        List<Link> linksWithUrl = linkRepository.getLinksByUrl(url);
        Link link;
        if (linksWithUrl.size() == 0) {
            if (parsingResult instanceof StackOverflowParsingResult stackOverflowParsingResult) {
                StackOverflowQuestionResponse response = stackOverflowClient.fetchQuestion(stackOverflowParsingResult.id());
                link = linkRepository.addLink(chatId, url, response.answerCount(), response.commentCount());
            } else {
                link = linkRepository.addLink(chatId, url);
            }
        } else {
            link = linksWithUrl.get(0);
        }
        try {
            chatLinkRepository.addChatLink(chatId, link.getId());
        } catch (Exception ignored) {}
        return link;
    }

    @Override
    @Transactional
    public Link remove(long chatId, URI url) throws LinkNotFoundException {
        List<Link> links = linkRepository.getLinksByUrl(url);
        if (links.size() == 0) {
            throw new LinkNotFoundException("Link not found: " + url);
        }
        Link link = links.get(0);
        chatLinkRepository.removeChatLink(chatId, link.getId());
        Long countRefs = chatLinkRepository.countChatByLinkId(link.getId());
        if (countRefs == 0) {
            linkRepository.removeLink(chatId, link.getId());
        }
        return link;
    }

    @Override
    public List<Link> listAll(long chatId) {
        return linkRepository.getAll(chatId);
    }
}

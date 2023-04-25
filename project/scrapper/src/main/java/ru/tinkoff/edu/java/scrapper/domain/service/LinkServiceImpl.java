package ru.tinkoff.edu.java.scrapper.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parser.Parser;
import results.ParsingResult;
import results.StackOverflowParsingResult;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final StackOverflowClient stackOverflowClient;
    private final Parser parser = new Parser();

    @Override
    public Link add(long chatId, URI url) {
        addTransactional(chatId, url);
        return linkRepository.getLinksByUrl(url).get(0);
    }

    @Transactional
    private void addTransactional(long chatId, URI url) {
        ParsingResult parsingResult = parser.parse(url.toString());
        if (parsingResult == null) {
            throw new IllegalArgumentException("Not supported link");
        }
        if (parsingResult instanceof StackOverflowParsingResult stackOverflowParsingResult) {
            StackOverflowQuestionResponse response = stackOverflowClient.fetchQuestion(stackOverflowParsingResult.id());
            linkRepository.addLinkIfNotExists(chatId, url, response.answerCount(), response.commentCount());
        } else {
            linkRepository.addLinkIfNotExists(chatId, url);
        }
        try {
            chatLinkRepository.addChatLinkByUrl(chatId, url);
        } catch (Exception ignored) {}
    }

    @Override
    @Transactional
    public Link remove(long chatId, URI url) throws LinkNotFoundException {
        List<Link> links = linkRepository.getLinksByUrl(url);
        if (links.size() == 0) {
            throw new LinkNotFoundException("Link not found: " + url);
        }
        chatLinkRepository.removeChatLinkByUrl(chatId, url);
        linkRepository.removeLinkIfNoOneRefers(chatId, url);
        return links.get(0);
    }

    @Override
    public List<Link> listAll(long chatId) {
        return linkRepository.getAll(chatId);
    }
}

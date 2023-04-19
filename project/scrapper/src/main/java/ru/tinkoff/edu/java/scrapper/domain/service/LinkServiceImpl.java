package ru.tinkoff.edu.java.scrapper.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parser.Parser;
import results.ParsingResult;
import results.StackOverflowParsingResult;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
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
    private final StackOverflowClient stackOverflowClient;
    private final Parser parser = new Parser();

    @Override
    public Link add(long chatId, URI url) {
        ParsingResult parsingResult = parser.parse(url.toString());
        if (parsingResult instanceof StackOverflowParsingResult stackOverflowParsingResult) {
            StackOverflowQuestionResponse response = stackOverflowClient.fetchQuestion(stackOverflowParsingResult.id());
            linkRepository.addAnswerComment(chatId, url, response.answerCount(), response.commentCount());
        } else {
            linkRepository.add(chatId, url);
        }
        return linkRepository.getLinksByUrl(url).get(0);
    }

    @Override
    public Link remove(long chatId, URI url) throws LinkNotFoundException {
        List<Link> links = linkRepository.getLinksByUrl(url);
        if (links.size() == 0) {
            throw new LinkNotFoundException("Link not found: " + url.toString());
        }
        linkRepository.remove(chatId, url);
        return links.get(0);
    }

    @Override
    public List<Link> listAll(long chatId) {
        return linkRepository.getAll(chatId);
    }
}

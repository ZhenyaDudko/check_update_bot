package ru.tinkoff.edu.java.scrapper.domain.jdbc;

import org.springframework.stereotype.Component;
import parser.Parser;
import results.ParsingResult;
import results.StackOverflowParsingResult;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkService;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

import java.net.URI;
import java.util.List;

@Component
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository linkRepository;
    private final StackOverflowClient stackOverflowClient;
    private final Parser parser = new Parser();

    public JdbcLinkService(JdbcLinkRepository linkRepository, StackOverflowClient stackOverflowClient) {
        this.linkRepository = linkRepository;
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public Link add(long chatId, URI url) {
        ParsingResult parsingResult = parser.parse(url.toString());
        if (parsingResult instanceof StackOverflowParsingResult stackOverflowParsingResult) {
            StackOverflowQuestionResponse response = stackOverflowClient.fetchQuestion(stackOverflowParsingResult.id());
            return linkRepository.addAnswerComment(chatId, url, response.answerCount(), response.commentCount());
        }
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

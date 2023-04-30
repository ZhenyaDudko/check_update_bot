package ru.tinkoff.edu.java.scrapper.domain.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import parser.Parser;
import results.ParsingResult;
import results.StackOverflowParsingResult;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Chat;
import ru.tinkoff.edu.java.scrapper.domain.jpa.repository.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.repository.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkService;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

import java.net.URI;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository jpaLinkRepository;
    private final JpaChatRepository jpaChatRepository;
    private final StackOverflowClient stackOverflowClient;
    private final Parser parser = new Parser();

    @Override
    @Transactional
    public Link add(long chatId, URI url) {
        ParsingResult parsingResult = parser.parse(url.toString());
        if (parsingResult == null) {
            throw new IllegalArgumentException("Not supported link");
        }
        Chat chat = jpaChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat with id: " + chatId + " not found"));
        var link = jpaLinkRepository.findByUrl(url.toString());
        if (link == null) {
            link = new ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Link().setUrl(url.toString());
            if (parsingResult instanceof StackOverflowParsingResult stackOverflowParsingResult) {
                StackOverflowQuestionResponse response = stackOverflowClient.fetchQuestion(stackOverflowParsingResult.id());
                link.setAnswerCount(response.answerCount()).setCommentCount(response.commentCount());
            }
        }
        chat.getLinks().add(link);
        return map(jpaLinkRepository.saveAndFlush(link));
    }

    @Override
    @Transactional
    public Link remove(long chatId, URI url) throws LinkNotFoundException {
        Chat chat = jpaChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat with id: " + chatId + " not found"));
        var link = jpaLinkRepository.findByUrl(url.toString());
        if (link == null || link.getChats().stream().noneMatch(c -> c.getId() == chatId)) {
            throw new LinkNotFoundException("Link not found: " + url);
        }
        chat.getLinks().remove(link);
        if (link.getChats().size() == 0) {
            jpaLinkRepository.delete(link);
        } else {
            jpaLinkRepository.save(link);
        }
        jpaLinkRepository.flush();
        return map(link);
    }

    @Override
    public List<Link> listAll(long chatId) {
        Chat chat = jpaChatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat with id: " + chatId + " not found"));
        return chat.getLinks().stream().map(JpaLinkService::map).toList();
    }

    protected static Link map(ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Link link) {
        return new Link()
                .setId(link.getId())
                .setUrl(URI.create(link.getUrl()))
                .setAnswerCount(link.getAnswerCount())
                .setCommentCount(link.getCommentCount())
                .setLastUpdate(link.getLastUpdate().atOffset(ZoneOffset.UTC));
    }
}

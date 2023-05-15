package ru.tinkoff.edu.java.scrapper.web.scheduler;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import parser.Parser;
import results.GithubParsingResult;
import results.ParsingResult;
import results.StackOverflowParsingResult;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.webclient.GithubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.web.notifier.UpdateNotifier;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final LinkUpdater linkUpdater;
    private final Parser parser = new Parser();
    private final GithubClient githubClient;
    private final StackOverflowClient stackOverflowClient;
    private final UpdateNotifier updateNotifier;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        log.info("Updated links.");
        List<Link> longAgoUpdatedLinks = linkUpdater.getLongAgoUpdated();
        for (Link link : longAgoUpdatedLinks) {
            ParsingResult parsingResult = parser.parse(link.getUrl().toString());
            OffsetDateTime time;
            String description = "The content on the link has been updated: " + link.getUrl().toString();
            if (parsingResult instanceof GithubParsingResult githubParsingResult) {
                GithubRepositoryResponse repositoryResponse =
                        githubClient.fetchRepository(githubParsingResult.user(), githubParsingResult.repository());
                time = repositoryResponse.updatedAt();
            } else if (parsingResult instanceof StackOverflowParsingResult stackOverflowParsingResult) {
                StackOverflowQuestionResponse questionResponse =
                        stackOverflowClient.fetchQuestion(stackOverflowParsingResult.id());
                time = questionResponse.updatedAt();
                if (questionResponse.answerCount() > link.getAnswerCount()) {
                    description = "New answers were added to the question: " + link.getUrl().toString();
                } else if (questionResponse.commentCount() > link.getCommentCount()) {
                    description = "New comments were added to the question: " + link.getUrl().toString();
                }
            } else {
                continue;
            }
            if (time.isAfter(link.getLastUpdate())) {
                List<Long> chatIds = linkUpdater.getChatsByLinkId(link.getId()).stream().map(Chat::getId).toList();
                updateNotifier.send(new LinkUpdateRequest(link.getId(), link.getUrl(), description, chatIds));
            }
        }
    }
}

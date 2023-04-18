package ru.tinkoff.edu.java.scrapper.web.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.BotClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class LinkUpdaterScheduler {

    private static final Logger log = LoggerFactory.getLogger(LinkUpdaterScheduler.class);

    private final LinkUpdater linkUpdater;
    private final Parser parser = new Parser();
    private final GithubClient githubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    public LinkUpdaterScheduler(
            LinkUpdater linkUpdater,
            GithubClient githubClient,
            StackOverflowClient stackOverflowClient,
            BotClient botClient) {

        this.linkUpdater = linkUpdater;
        this.githubClient = githubClient;
        this.stackOverflowClient = stackOverflowClient;
        this.botClient = botClient;
    }

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        log.info("Updated links.");
        List<Link> longAgoUpdatedLinks = linkUpdater.getLongAgoUpdated();
        for (Link link : longAgoUpdatedLinks) {
            ParsingResult parsingResult = parser.parse(link.getUrl().toString());
            OffsetDateTime time;
            if (parsingResult instanceof GithubParsingResult githubParsingResult) {
                GithubRepositoryResponse repositoryResponse =
                        githubClient.fetchRepository(githubParsingResult.user(), githubParsingResult.repository());
                time = repositoryResponse.updatedAt();
            } else if (parsingResult instanceof StackOverflowParsingResult stackOverflowParsingResult) {
                StackOverflowQuestionResponse questionResponse =
                        stackOverflowClient.fetchQuestion(stackOverflowParsingResult.id());
                time = questionResponse.updatedAt();
            } else {
                continue;
            }
            if (time.isAfter(link.getLastUpdate())) {
                List<Long> chatIds = linkUpdater.getChatsByLinkId(link.getId()).stream().map(Chat::getId).toList();
                botClient.LinkUpdate(link.getId(), link.getUrl(),
                        "The content on the link has been updated: " + link.getUrl().toString(), chatIds);
            }
        }
    }
}

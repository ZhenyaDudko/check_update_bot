package ru.tinkoff.edu.java.scrapper.web.notifier;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.queue.ScrapperQueueProducer;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.BotClient;

@Service
public class UpdateNotifier {

    private final BotClient botClient;
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final boolean useQueue;

    public UpdateNotifier(
            ApplicationConfig applicationConfig,
            BotClient botClient,
            ScrapperQueueProducer scrapperQueueProducer
    ) {
        this.useQueue = applicationConfig.useQueue();
        this.botClient = botClient;
        this.scrapperQueueProducer = scrapperQueueProducer;
    }

    public void send(LinkUpdateRequest update) {
        if (useQueue) {
            scrapperQueueProducer.send(update);
        } else {
            botClient.linkUpdate(update);
        }
    }
}

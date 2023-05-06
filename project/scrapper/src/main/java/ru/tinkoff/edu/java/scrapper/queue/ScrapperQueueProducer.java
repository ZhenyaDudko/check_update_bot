package ru.tinkoff.edu.java.scrapper.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.web.notifier.UpdateNotifier;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements UpdateNotifier {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationConfig applicationConfig;

    public void send(LinkUpdateRequest update) {
        rabbitTemplate.convertAndSend(applicationConfig.exchangeName(), applicationConfig.routingKey(), update);
    }
}

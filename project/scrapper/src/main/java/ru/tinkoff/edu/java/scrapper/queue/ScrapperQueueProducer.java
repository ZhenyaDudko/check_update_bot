package ru.tinkoff.edu.java.scrapper.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(LinkUpdateRequest update) {
        rabbitTemplate.convertAndSend("exchange", "key", update);
    }
}

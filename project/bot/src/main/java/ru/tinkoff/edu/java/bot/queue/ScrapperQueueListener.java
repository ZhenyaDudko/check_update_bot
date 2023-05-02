package ru.tinkoff.edu.java.bot.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.web.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.update_handler.UpdateHandler;

@RabbitListener(queues = "${app.queue-name:queue}")
@Component
@RequiredArgsConstructor
public class ScrapperQueueListener {

    private final UpdateHandler updateHandler;

    @RabbitHandler
    public void receiver(LinkUpdateRequest update) {
        updateHandler.handle(update);
    }
}

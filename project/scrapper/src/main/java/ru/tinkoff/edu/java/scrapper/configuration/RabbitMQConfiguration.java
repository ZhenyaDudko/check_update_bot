package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.queue.ScrapperQueueProducer;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfiguration {

    private final ApplicationConfig applicationConfig;
    private static final String DEAD_LETTER_QUEUE_NAMING_SUFFIX = ".dlq";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(applicationConfig.exchangeName());
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable(applicationConfig.queueName())
                .withArgument("x-dead-letter-exchange", applicationConfig.exchangeName() +
                        DEAD_LETTER_QUEUE_NAMING_SUFFIX)
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(applicationConfig.routingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public ScrapperQueueProducer scrapperQueueProducer(RabbitTemplate rabbitTemplate) {
        return new ScrapperQueueProducer(rabbitTemplate, applicationConfig);
    }
}

package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.web.LinkUpdateRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfiguration {

    private final ApplicationConfig applicationConfig;
    private static final String DEAD_LETTER_QUEUE_NAMING_SUFFIX = ".dlq";

    @Bean
    public DirectExchange exchangeDLQ() {
        return new DirectExchange(applicationConfig.exchangeName() +
                DEAD_LETTER_QUEUE_NAMING_SUFFIX);
    }

    @Bean
    public Queue queueDLQ() {
        return QueueBuilder.durable(applicationConfig.queueName() +
                DEAD_LETTER_QUEUE_NAMING_SUFFIX).build();
    }

    @Bean
    public Binding bindingDLQ() {
        return BindingBuilder.bind(queueDLQ()).to(exchangeDLQ()).with(applicationConfig.routingKey());
    }

    @Bean
    public ClassMapper classMapper() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest", LinkUpdateRequest.class);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.java.scrapper.dto.webclient.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }
}

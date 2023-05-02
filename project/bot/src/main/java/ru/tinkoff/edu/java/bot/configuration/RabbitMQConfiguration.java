package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.web.LinkUpdateRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {

    @Autowired
    ApplicationConfig applicationConfig;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(applicationConfig.exchangeName());
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable(applicationConfig.queueName())
                .withArgument("x-dead-letter-exchange", applicationConfig.exchangeName() + ".dlq")
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("key");
    }

    @Bean
    public DirectExchange exchangeDLQ() {
        return new DirectExchange(applicationConfig.exchangeName() + ".dlq");
    }

    @Bean
    public Queue queueDLQ() {
        return QueueBuilder.durable(applicationConfig.queueName() + ".dlq").build();
    }

    @Bean
    public Binding bindingDLQ() {
        return BindingBuilder.bind(queueDLQ()).to(exchangeDLQ()).with("key");
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

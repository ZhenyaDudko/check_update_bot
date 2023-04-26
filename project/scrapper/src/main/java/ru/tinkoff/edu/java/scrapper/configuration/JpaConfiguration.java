package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.tinkoff.edu.java.scrapper.domain.jpa.repository.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.repository.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jpa.service.JpaChatService;
import ru.tinkoff.edu.java.scrapper.domain.jpa.service.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.domain.jpa.service.JpaLinkUpdaterService;
import ru.tinkoff.edu.java.scrapper.domain.service.*;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

@Configuration
@EnableTransactionManagement
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfiguration {

    @Bean
    public ChatService chatService(JpaChatRepository jpaChatRepository) {
        return new JpaChatService(jpaChatRepository);
    }

    @Bean
    public LinkService linkService(
            JpaLinkRepository jpaLinkRepository,
            JpaChatRepository jpaChatRepository,
            StackOverflowClient stackOverflowClient) {
        return new JpaLinkService(jpaLinkRepository, jpaChatRepository, stackOverflowClient);
    }

    @Bean
    public LinkUpdater linkUpdater(
            JpaLinkRepository jpaLinkRepository) {
        return new JpaLinkUpdaterService(jpaLinkRepository);
    }
}

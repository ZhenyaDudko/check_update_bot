package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.DSLContext;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.tinkoff.edu.java.scrapper.domain.jooq.repository.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.repository.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.repository.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.ChatServiceImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.LinkServiceImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.LinkUpdaterImpl;
import ru.tinkoff.edu.java.scrapper.domain.service.*;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@EnableTransactionManagement
public class JooqAccessConfiguration {

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
                .withRenderSchema(false)
                .withRenderFormatted(true)
                .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    public JooqChatRepository jooqChatRepository(DSLContext dsl) {
        return new JooqChatRepository(dsl);
    }

    @Bean
    public JooqLinkRepository jooqLinkRepository(DSLContext dsl) {
        return new JooqLinkRepository(dsl);
    }

    @Bean
    public JooqChatLinkRepository jooqChatLinkRepository(DSLContext dsl) {
        return new JooqChatLinkRepository(dsl);
    }

    @Bean
    public ChatService chatService(
            JooqChatRepository chatRepository,
            JooqLinkRepository linkRepository,
            JooqChatLinkRepository chatLinkRepository) {
        return new ChatServiceImpl(chatRepository, linkRepository, chatLinkRepository);
    }

    @Bean
    public LinkService linkService(
            JooqLinkRepository linkRepository,
            JooqChatLinkRepository chatLinkRepository,
            StackOverflowClient stackOverflowClient) {
        return new LinkServiceImpl(linkRepository, chatLinkRepository, stackOverflowClient);
    }

    @Bean
    public LinkUpdater linkUpdater(
            JooqLinkRepository linkRepository,
            JooqChatLinkRepository chatLinkRepository) {
        return new LinkUpdaterImpl(linkRepository, chatLinkRepository);
    }

}

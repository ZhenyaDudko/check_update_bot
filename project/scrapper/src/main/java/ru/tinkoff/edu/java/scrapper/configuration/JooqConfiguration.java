package ru.tinkoff.edu.java.scrapper.configuration;

import org.jooq.SQLDialect;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.tinkoff.edu.java.scrapper.domain.jooq.repository.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.repository.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.repository.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.ChatServiceImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.LinkServiceImpl;
import ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service.LinkUpdaterImpl;
import ru.tinkoff.edu.java.scrapper.domain.service.*;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@EnableTransactionManagement
public class JooqConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setUser(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    @Bean
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.settings()
                .withRenderSchema(false)
                .withRenderFormatted(true)
                .withRenderQuotedNames(RenderQuotedNames.NEVER);

        SQLDialect sqlDialectName = SQLDialect.POSTGRES;
        jooqConfiguration.set(sqlDialectName);

        return jooqConfiguration;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
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

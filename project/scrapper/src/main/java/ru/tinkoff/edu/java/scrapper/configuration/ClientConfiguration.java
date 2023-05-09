package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.BotClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.BotClientImpl;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.GithubClientImpl;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.StackOverflowClientImpl;

@Configuration
public class ClientConfiguration {

    @Value("${githubClient.baseurl:" + GithubClientImpl.BASE_URL + "}")
    private String githubBaseUrl;

    @Value("${stackOverflowClient.baseurl:" + StackOverflowClientImpl.BASE_URL + "}")
    private String stackOverflowBaseUrl;

    @Value("${botClient.baseurl:" + BotClientImpl.BASE_URL + "}")
    private String botBaseUrl;

    @Bean
    public GithubClient githubClient() {
        return new GithubClientImpl(githubBaseUrl);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClientImpl(stackOverflowBaseUrl);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public BotClient botClient() {
        return new BotClientImpl(botBaseUrl);
    }

}

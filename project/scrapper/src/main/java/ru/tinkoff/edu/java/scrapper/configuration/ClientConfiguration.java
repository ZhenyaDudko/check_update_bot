package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.web.webclient.client.*;

@Configuration
public class ClientConfiguration {

    @Value("${githubClient.baseurl:" + GithubClientImpl.baseUrl + "}")
    private String githubBaseUrl;

    @Value("${stackOverflowClient.baseurl:" + StackOverflowClientImpl.baseUrl + "}")
    private String stackOverflowBaseUrl;

    @Value("${botClient.baseurl:" + BotClientImpl.baseUrl + "}")
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
    public BotClient botClient() {
        return new BotClientImpl(botBaseUrl);
    }

}

package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.dto.GithubRepositoryResponse;

@Component
public class GithubClientImpl implements GithubClient {

    public static final String baseUrl = "https://api.github.com/repos";

    private static final String urlTemplate = "/{user}/{repository}";

    private final WebClient webClient;

    public GithubClientImpl(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public GithubClientImpl() {
        this.webClient = WebClient.create(baseUrl);
    }

    @Override
    public GithubRepositoryResponse fetchRepository(String user, String repository) {
        return webClient.get()
                .uri(urlTemplate, user, repository)
                .retrieve()
                .bodyToMono(GithubRepositoryResponse.class)
                .block();
    }

}

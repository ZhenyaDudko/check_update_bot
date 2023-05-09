package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.webclient.GithubRepositoryResponse;

@Component
public class GithubClientImpl implements GithubClient {

    public static final String BASE_URL = "https://api.github.com/repos";

    private static final String URL_TEMPLATE = "/{user}/{repository}";

    private final WebClient webClient;

    public GithubClientImpl(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public GithubClientImpl() {
        this.webClient = WebClient.create(BASE_URL);
    }

    @Override
    public GithubRepositoryResponse fetchRepository(String user, String repository) {
        return webClient.get()
                .uri(URL_TEMPLATE, user, repository)
                .retrieve()
                .bodyToMono(GithubRepositoryResponse.class)
                .block();
    }

}

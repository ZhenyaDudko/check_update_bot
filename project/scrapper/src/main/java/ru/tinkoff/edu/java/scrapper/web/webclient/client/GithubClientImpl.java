package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.dto.GithubRepositoryResponse;

public class GithubClientImpl implements GithubClient {

    private static final String urlTemplate = "https://api.github.com/repos/%s/%s";

    private final WebClient webClient;

    public GithubClientImpl(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public GithubClientImpl() {
        String defaultUrl = String.format(urlTemplate, "spring-projects", "spring-boot");
        this.webClient = WebClient.create(defaultUrl);
    }

    @Override
    public GithubRepositoryResponse fetchRepository(String user, String repository) {
        return webClient.get()
                .uri(String.format(urlTemplate, user, repository))
                .retrieve()
                .bodyToMono(GithubRepositoryResponse.class)
                .block();
    }

}

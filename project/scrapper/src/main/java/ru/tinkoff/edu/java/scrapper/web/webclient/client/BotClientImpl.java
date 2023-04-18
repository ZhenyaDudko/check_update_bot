package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest;

import java.net.URI;
import java.util.List;

public class BotClientImpl implements BotClient {

    public static final String baseUrl = "http://localhost:8080";

    private final WebClient webClient;

    public BotClientImpl(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public BotClientImpl() {
        this.webClient = WebClient.create(baseUrl);
    }

    @Override
    public void LinkUpdate(long id, URI url, String description, List<Long> chatIds) {
        webClient.post().uri("/updates")
                .bodyValue(new LinkUpdateRequest(id, url, description, chatIds))
                .retrieve();
    }
}

package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.dto.StackOverflowQuestionResponse;
import java.util.List;

public class StackOverflowClientImpl implements StackOverflowClient {

    public static final String baseUrl = "https://api.stackexchange.com/questions";

    private final WebClient webClient;

    public StackOverflowClientImpl(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public StackOverflowClientImpl() {
        this.webClient = WebClient.create(baseUrl);
    }

    @Override
    public StackOverflowQuestionResponse fetchQuestion(String id) {
        Wrapper wrapper = webClient.get()
                .uri(builder -> builder
                        .pathSegment("{id}")
                        .queryParam("site", "stackoverflow")
                        .build(id))
                .retrieve()
                .bodyToMono(Wrapper.class)
                .block();
        if (wrapper == null || wrapper.items.isEmpty()) {
            return null;
        }
        return wrapper.items.get(0);
    }

    private record Wrapper(List<StackOverflowQuestionResponse> items) {
    }

}

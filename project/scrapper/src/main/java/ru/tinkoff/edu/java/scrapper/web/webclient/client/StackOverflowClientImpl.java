package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.web.webclient.dto.StackOverflowQuestionResponse;
import java.util.List;

public class StackOverflowClientImpl implements StackOverflowClient {

    private final WebClient webClient;

    private final String urlTemplate = "https://api.stackexchange.com/questions/%s?site=stackoverflow";

    public StackOverflowClientImpl(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public StackOverflowClientImpl() {
        String defaultUrl = String.format(urlTemplate, "4");
        this.webClient = WebClient.create(defaultUrl);
    }

    @Override
    public StackOverflowQuestionResponse fetchQuestion(String id) {
        Wrapper wrapper = webClient.get()
                .uri(String.format(urlTemplate, id))
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

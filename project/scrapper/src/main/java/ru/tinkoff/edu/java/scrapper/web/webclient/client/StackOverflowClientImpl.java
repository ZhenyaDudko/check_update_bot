package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;

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
        WrapperQuestion wrapperQuestion = webClient.get()
                .uri(builder -> builder
                        .pathSegment("{id}")
                        .queryParam("site", "stackoverflow")
                        .build(id))
                .retrieve()
                .bodyToMono(WrapperQuestion.class)
                .block();
        if (wrapperQuestion == null || wrapperQuestion.items.isEmpty()) {
            return null;
        }
        WrapperComments wrapperComments = webClient.get()
                .uri(builder -> builder
                        .pathSegment("{id}")
                        .pathSegment("comments")
                        .queryParam("site", "stackoverflow")
                        .build(id))
                .retrieve()
                .bodyToMono(WrapperComments.class)
                .block();
        if (wrapperComments == null) {
            return null;
        }
        StackOverflowQuestionResponse questionResponse = wrapperQuestion.items.get(0);
        return new StackOverflowQuestionResponse(
                questionResponse.updatedAt(),
                questionResponse.title(),
                questionResponse.answerCount(),
                wrapperComments.items.size());
    }

    private record WrapperQuestion(List<StackOverflowQuestionResponse> items) {
    }

    private record WrapperComments(List<Object> items) {
    }

}

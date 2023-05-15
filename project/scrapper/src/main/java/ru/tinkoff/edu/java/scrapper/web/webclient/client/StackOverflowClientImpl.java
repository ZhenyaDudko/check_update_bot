package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;

public class StackOverflowClientImpl implements StackOverflowClient {

    public static final String BASE_URL = "https://api.stackexchange.com/questions";

    private final WebClient webClient;

    public StackOverflowClientImpl(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public StackOverflowClientImpl() {
        this.webClient = WebClient.create(BASE_URL);
    }

    @Override
    public StackOverflowQuestionResponse fetchQuestion(String id) {
        String idPathFragment = "{id}";
        String siteParam = "site";
        String site = "stackoverflow";

        WrapperQuestion wrapperQuestion = webClient.get()
                .uri(builder -> builder
                        .pathSegment(idPathFragment)
                        .queryParam(siteParam, site)
                        .build(id))
                .retrieve()
                .bodyToMono(WrapperQuestion.class)
                .block();
        if (wrapperQuestion == null || wrapperQuestion.items.isEmpty()) {
            return null;
        }
        WrapperComments wrapperComments = webClient.get()
                .uri(builder -> builder
                        .pathSegment(idPathFragment)
                        .pathSegment("comments")
                        .queryParam(siteParam, site)
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

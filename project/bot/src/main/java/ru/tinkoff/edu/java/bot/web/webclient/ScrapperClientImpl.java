package ru.tinkoff.edu.java.bot.web.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.web.*;
import ru.tinkoff.edu.java.bot.exceptions.web.IncorrectRequestParametersException;
import ru.tinkoff.edu.java.bot.exceptions.web.NotFoundException;

public class ScrapperClientImpl implements ScrapperClient {

    public static final String baseUrl = "http://localhost:8080";

    private final WebClient webClient;

    public ScrapperClientImpl(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public ScrapperClientImpl() {
        this.webClient = WebClient.create(baseUrl);
    }

    @Override
    public void registerChat(long id) throws Throwable {
        chatRequest(HttpMethod.POST, id);
    }

    @Override
    public void deleteChat(long id) throws Throwable {
        chatRequest(HttpMethod.DELETE, id);
    }

    @Override
    public ListLinksResponse getLinks(long chatId) throws Throwable {
        return linksRequestFinish(
                linksRequestStart(HttpMethod.GET, chatId),
                ListLinksResponse.class
        );
    }

    @Override
    public LinkResponse addLink(long chatId, String link) throws Throwable {
        return linksRequestFinish(
                linksRequestStart(HttpMethod.POST, chatId).bodyValue(new AddLinkRequest(link)),
                LinkResponse.class
        );
    }

    @Override
    public LinkResponse deleteLink(long chatId, String link) throws Throwable {
        return linksRequestFinish(
                linksRequestStart(HttpMethod.DELETE, chatId).bodyValue(new RemoveLinkRequest(link)),
                LinkResponse.class
        );
    }

    private void chatRequest(HttpMethod method, long id) throws Throwable {
        finishAndUnwrapException(
                onStatus(webClient.method(method)
                        .uri(builder -> builder.pathSegment("tg-chat", "{id}").build(id))
                        .retrieve())
                        .toBodilessEntity());
    }

    private WebClient.RequestBodySpec linksRequestStart(HttpMethod method, long chatId) {
        return webClient.method(method)
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId));
    }

    private <T> T linksRequestFinish(WebClient.RequestHeadersSpec<?> requestBodySpec, Class<T> tClass) throws Throwable {
        return finishAndUnwrapException(onStatus(requestBodySpec.retrieve()).bodyToMono(tClass));
    }

    private WebClient.ResponseSpec onStatus(WebClient.ResponseSpec responseSpec) {
        return responseSpec
                .onStatus(HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(ApiErrorResponse.class)
                                .map(val -> new IncorrectRequestParametersException(val.exceptionMessage())))
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(ApiErrorResponse.class)
                                .map(val -> new NotFoundException(val.exceptionMessage())));
    }

    private <T> T finishAndUnwrapException(Mono<T> result) throws Throwable {
        try {
            return result.block();
        } catch (Exception e) {
            throw Exceptions.unwrap(e);
        }
    }
}

package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import ru.tinkoff.edu.java.scrapper.web.webclient.dto.StackOverflowQuestionResponse;

public interface StackOverflowClient {

    StackOverflowQuestionResponse fetchQuestion(String id);
}

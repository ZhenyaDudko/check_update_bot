package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import ru.tinkoff.edu.java.scrapper.dto.webclient.StackOverflowQuestionResponse;

public interface StackOverflowClient {

    StackOverflowQuestionResponse fetchQuestion(String id);
}

package ru.tinkoff.edu.java.scrapper.domain.repository;

import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;

public interface ChatRepository {

    void add(long chatId);
    void remove(long chatId) throws ChatNotFoundException;

    Long countChatById(long chatId);
}

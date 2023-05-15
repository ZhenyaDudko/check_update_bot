package ru.tinkoff.edu.java.scrapper.domain.service;

import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;

public interface ChatService {
    void register(long chatId);

    void unregister(long chatId) throws ChatNotFoundException;
}

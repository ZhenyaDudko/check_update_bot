package ru.tinkoff.edu.java.scrapper.domain.repository;

public interface ChatRepository {

    void add(long chatId);

    void remove(long chatId);

    Long countChatById(long chatId);
}

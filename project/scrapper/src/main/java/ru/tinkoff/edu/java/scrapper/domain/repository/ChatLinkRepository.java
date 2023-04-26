package ru.tinkoff.edu.java.scrapper.domain.repository;

import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;

import java.util.List;

public interface ChatLinkRepository {

    void addChatLink(long chatId, long linkId);

    void removeChatLink(long chatId, long linkId);

    Long countChatByLinkId(long linkId);

    List<Chat> getChatsByLinkId(long id);
}

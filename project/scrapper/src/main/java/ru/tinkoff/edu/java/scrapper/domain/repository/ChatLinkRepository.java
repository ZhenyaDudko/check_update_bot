package ru.tinkoff.edu.java.scrapper.domain.repository;

import java.net.URI;

public interface ChatLinkRepository {

    void addChatLinkByUrl(long chatId, URI url);

    void removeChatLinkByUrl(long chatId, URI url);
}

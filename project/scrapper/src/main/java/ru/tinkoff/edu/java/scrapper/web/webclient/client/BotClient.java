package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest;

public interface BotClient {
    void linkUpdate(LinkUpdateRequest update);
}

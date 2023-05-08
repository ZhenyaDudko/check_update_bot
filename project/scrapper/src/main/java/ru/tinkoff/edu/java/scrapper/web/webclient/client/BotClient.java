package ru.tinkoff.edu.java.scrapper.web.webclient.client;

import ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.web.notifier.UpdateNotifier;

public interface BotClient extends UpdateNotifier {
    void send(LinkUpdateRequest update);
}

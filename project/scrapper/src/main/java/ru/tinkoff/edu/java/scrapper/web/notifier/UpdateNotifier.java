package ru.tinkoff.edu.java.scrapper.web.notifier;

import ru.tinkoff.edu.java.scrapper.dto.webclient.LinkUpdateRequest;

public interface UpdateNotifier {
    void send(LinkUpdateRequest update);
}

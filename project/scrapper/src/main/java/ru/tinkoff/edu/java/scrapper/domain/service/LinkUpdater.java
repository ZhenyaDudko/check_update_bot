package ru.tinkoff.edu.java.scrapper.domain.service;

import java.time.OffsetDateTime;
import java.util.List;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

public interface LinkUpdater {
    void updateTimeByLinkId(long id, OffsetDateTime time);

    List<Link> getLongAgoUpdated();

    List<Chat> getChatsByLinkId(long id);
}

package ru.tinkoff.edu.java.scrapper.domain.service;

import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {
    void updateTimeByLinkId(long id, OffsetDateTime time);
    List<Link> getLongAgoUpdated();
    List<Chat> getChatsByLinkId(long id);
}

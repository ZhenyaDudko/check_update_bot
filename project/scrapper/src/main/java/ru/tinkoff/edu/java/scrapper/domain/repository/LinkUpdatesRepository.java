package ru.tinkoff.edu.java.scrapper.domain.repository;

import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdatesRepository {

    void updateTimeByLinkId(long id, OffsetDateTime time);
    List<Link> getLongAgoUpdated();
    List<Chat> getChatsByLinkId(long id);
}

package ru.tinkoff.edu.java.scrapper.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkUpdatesRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkUpdaterImpl implements LinkUpdater {

    private final LinkUpdatesRepository linkUpdatesRepository;

    @Override
    public void updateTimeByLinkId(long id, OffsetDateTime time) {
        linkUpdatesRepository.updateTimeByLinkId(id, time);
    }

    @Override
    public List<Link> getLongAgoUpdated() {
        return linkUpdatesRepository.getLongAgoUpdated();
    }

    @Override
    public List<Chat> getChatsByLinkId(long id) {
        return linkUpdatesRepository.getChatsByLinkId(id);
    }
}

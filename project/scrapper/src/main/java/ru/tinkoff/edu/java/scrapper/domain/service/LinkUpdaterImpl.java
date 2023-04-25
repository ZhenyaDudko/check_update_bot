package ru.tinkoff.edu.java.scrapper.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkUpdaterImpl implements LinkUpdater {

    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Override
    public void updateTimeByLinkId(long id, OffsetDateTime time) {
        linkRepository.updateTimeByLinkId(id, time);
    }

    @Override
    public List<Link> getLongAgoUpdated() {
        return linkRepository.getLongAgoUpdated();
    }

    @Override
    public List<Chat> getChatsByLinkId(long id) {
        return chatLinkRepository.getChatsByLinkId(id);
    }
}

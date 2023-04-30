package ru.tinkoff.edu.java.scrapper.domain.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.jpa.repository.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class JpaLinkUpdaterService implements LinkUpdater {

    private final JpaLinkRepository jpaLinkRepository;

    @Override
    @Transactional
    public void updateTimeByLinkId(long id, OffsetDateTime time) {
        var link = jpaLinkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Link with id: " + id + " not exists"));
        link.setLastUpdate(time.toLocalDateTime());
        jpaLinkRepository.saveAndFlush(link);
    }

    @Override
    @Transactional
    public List<Link> getLongAgoUpdated() {
        return jpaLinkRepository.findAllByLastUpdateBefore(LocalDateTime.now().minusHours(1))
                .stream().map(JpaLinkService::map).toList();
    }

    @Override
    public List<Chat> getChatsByLinkId(long id) {
        var link = jpaLinkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Link with id: " + id + " not exists"));

        return link.getChats().stream().map(c -> new Chat(c.getId())).toList();
    }
}

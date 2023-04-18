package ru.tinkoff.edu.java.scrapper.domain.jdbc;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.repository.JdbcLinkUpdatesRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class JdbcLinkUpdater implements LinkUpdater {

    private final JdbcLinkUpdatesRepository linkUpdatesRepository;

    public JdbcLinkUpdater(JdbcLinkUpdatesRepository linkUpdatesRepository) {
        this.linkUpdatesRepository = linkUpdatesRepository;
    }

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

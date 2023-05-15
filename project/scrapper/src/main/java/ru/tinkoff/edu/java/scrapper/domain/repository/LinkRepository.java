package ru.tinkoff.edu.java.scrapper.domain.repository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

public interface LinkRepository {

    Link addLink(long chatId, URI url);

    Link addLink(long chatId, URI url, Integer answerCount, Integer commentCount);

    void removeLink(long chatId, long id);

    Long getIdByUrl(URI url);

    List<Link> getLinksByUrl(URI url);

    List<Link> getAll(long chatId);

    void updateTimeByLinkId(long id, OffsetDateTime time);

    List<Link> getLongAgoUpdated();
}

package ru.tinkoff.edu.java.scrapper.domain.jooq.repository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.types.DayToSecond;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.ChatLink;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.Link;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRecordMapper;
import static org.jooq.impl.DSL.select;

@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {

    private final DSLContext dsl;

    @Override
    public ru.tinkoff.edu.java.scrapper.dto.domain.Link addLink(long chatId, URI url) {
        return addLink(chatId, url, null, null);
    }

    @Override
    public ru.tinkoff.edu.java.scrapper.dto.domain.Link addLink(
            long chatId, URI url, Integer answerCount, Integer commentCount) {
        Link linkTable = Link.LINK;
        var linkRecordMapper = new LinkRecordMapper();

        return dsl.insertInto(linkTable, linkTable.URL, linkTable.ANSWER_COUNT, linkTable.COMMENT_COUNT)
                .values(url.toString(), answerCount, commentCount)
                .returning(
                        linkTable.ID,
                        linkTable.URL,
                        linkTable.LAST_UPDATE,
                        linkTable.ANSWER_COUNT,
                        linkTable.COMMENT_COUNT)
                .fetchInto(LinkRecord.class).stream().map(linkRecordMapper::map).toList().get(0);
    }

    @Override
    public void removeLink(long chatId, long linkId) throws LinkNotFoundException {
        Link linkTable = Link.LINK;

        dsl.delete(linkTable)
                .where(linkTable.ID.eq(linkId))
                .execute();
    }

    @Override
    public Long getIdByUrl(URI url) {
        Link linkTable = Link.LINK;

        return dsl.fetchValue(select(linkTable.ID)
                .from(linkTable)
                .where(linkTable.URL.eq(url.toString())));
    }

    @Override
    public List<ru.tinkoff.edu.java.scrapper.dto.domain.Link> getLinksByUrl(URI url) {
        Link linkTable = Link.LINK;
        var linkRecordMapper = new LinkRecordMapper();
        return dsl.select().from(linkTable).where(linkTable.URL.eq(url.toString()))
                .fetchInto(LinkRecord.class).stream().map(linkRecordMapper::map).toList();
    }

    @Override
    public List<ru.tinkoff.edu.java.scrapper.dto.domain.Link> getAll(long chatId) {
        ChatLink chatLinkTable = ChatLink.CHAT_LINK;
        Link linkTable = Link.LINK;
        var linkRecordMapper = new LinkRecordMapper();

        return dsl.select(linkTable.ID,
                        linkTable.URL,
                        linkTable.LAST_UPDATE,
                        linkTable.ANSWER_COUNT,
                        linkTable.COMMENT_COUNT)
                .from(chatLinkTable)
                .join(linkTable)
                .on(chatLinkTable.LINK_ID.eq(linkTable.ID))
                .where(chatLinkTable.CHAT_ID.eq(chatId))
                .fetchInto(LinkRecord.class).stream().map(linkRecordMapper::map).toList();
    }

    @Override
    public void updateTimeByLinkId(long id, OffsetDateTime time) {
        Link linkTable = Link.LINK;
        dsl.update(linkTable)
                .set(linkTable.LAST_UPDATE, time.toLocalDateTime())
                .where(linkTable.ID.eq(id))
                .execute();
    }

    @Override
    public List<ru.tinkoff.edu.java.scrapper.dto.domain.Link> getLongAgoUpdated() {
        Link linkTable = Link.LINK;
        LinkRecordMapper linkRecordMapper = new LinkRecordMapper();
        return dsl.select()
                .from(linkTable)
                .where(linkTable.LAST_UPDATE.lt(DSL.currentLocalDateTime().minus(new DayToSecond(0, 1))))
                .fetchInto(LinkRecord.class).stream().map(linkRecordMapper::map).toList();
    }
}

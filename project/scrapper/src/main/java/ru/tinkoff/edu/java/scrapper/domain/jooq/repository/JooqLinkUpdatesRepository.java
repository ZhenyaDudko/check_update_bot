package ru.tinkoff.edu.java.scrapper.domain.jooq.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.types.DayToSecond;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.ChatLink;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkUpdatesRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRecordMapper;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class JooqLinkUpdatesRepository implements LinkUpdatesRepository {

    private final DSLContext dsl;

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

    @Override
    public List<Chat> getChatsByLinkId(long id) {
        ChatLink chatLinkTable = ChatLink.CHAT_LINK;
        return dsl.select(chatLinkTable.CHAT_ID)
                .from(chatLinkTable)
                .where(chatLinkTable.LINK_ID.eq(id))
                .fetchInto(Chat.class);
    }
}

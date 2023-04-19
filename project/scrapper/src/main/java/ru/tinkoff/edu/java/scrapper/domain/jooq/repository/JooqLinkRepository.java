package ru.tinkoff.edu.java.scrapper.domain.jooq.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.ChatLink;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRecordMapper;

import java.net.URI;
import java.util.List;

import static org.jooq.impl.DSL.*;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {

    private final DSLContext dsl;

    @Override
    public void add(long chatId, URI url) {
        addAnswerComment(chatId, url, null, null);
    }

    @Override
    @Transactional
    public void addAnswerComment(long chatId, URI url, Integer answerCount, Integer commentCount) {
        Link linkTable = Link.LINK;

        var selectValuesIfLinkNotExists =
                dsl.select(val(url.toString()), val(answerCount), val(commentCount))
                        .where(notExists(dsl.selectOne().from(linkTable).where(linkTable.URL.eq(url.toString()))));

        dsl.insertInto(linkTable, linkTable.URL, linkTable.ANSWER_COUNT, linkTable.COMMENT_COUNT)
                .select(selectValuesIfLinkNotExists)
                .execute();

        ChatLink chatLinkTable = ChatLink.CHAT_LINK;

        var selectLinkIdByUrl =
                dsl.select(linkTable.ID)
                        .from(linkTable)
                        .where(linkTable.URL.eq(url.toString()));

        dsl.insertInto(chatLinkTable, chatLinkTable.CHAT_ID, chatLinkTable.LINK_ID)
                .values(val(chatId), DSL.field(selectLinkIdByUrl))
                .execute();
    }

    @Override
    public void remove(long chatId, URI url) throws LinkNotFoundException {
        ChatLink chatLinkTable = ChatLink.CHAT_LINK;
        Link linkTable = Link.LINK;

        var selectLinkIdByUrl =
                dsl.select(linkTable.ID)
                        .from(linkTable)
                        .where(linkTable.URL.eq(url.toString()));

        dsl.deleteFrom(chatLinkTable)
                .where(chatLinkTable.CHAT_ID.eq(chatId).and(chatLinkTable.LINK_ID.in(selectLinkIdByUrl)))
                .execute();

        var countLinkRefs = dsl.selectCount()
                .from(chatLinkTable)
                .join(linkTable)
                .on(chatLinkTable.LINK_ID.eq(linkTable.ID))
                .where(linkTable.URL.eq(url.toString()));

        var getCountResultFromWithClause =
                dsl.select(field(name("cnt"))).from(table(name("count_refs")));

        dsl.with("count_refs").as(countLinkRefs)
                .delete(linkTable)
                .where(field(getCountResultFromWithClause).eq(0)
                        .and(linkTable.URL.eq(url.toString())))
                .execute();
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

        return dsl.select(chatLinkTable.LINK_ID.as("link_id"),
                        linkTable.URL.as("url"),
                        linkTable.LAST_UPDATE.as("last_update"),
                        linkTable.ANSWER_COUNT.as("answer_count"),
                        linkTable.COMMENT_COUNT.as("comment_count"))
                .from(chatLinkTable)
                .join(linkTable)
                .on(chatLinkTable.LINK_ID.eq(linkTable.ID))
                .where(chatLinkTable.CHAT_ID.eq(chatId))
                .fetchInto(LinkRecord.class).stream().map(linkRecordMapper::map).toList();
    }
}

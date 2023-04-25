package ru.tinkoff.edu.java.scrapper.domain.jooq.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.ChatLink;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;

import java.net.URI;

import static org.jooq.impl.DSL.val;

@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository {

    private final DSLContext dsl;

    @Override
    public void addChatLinkByUrl(long chatId, URI url) {
        ChatLink chatLinkTable = ChatLink.CHAT_LINK;
        Link linkTable = Link.LINK;

        var selectLinkIdByUrl =
                dsl.select(linkTable.ID)
                        .from(linkTable)
                        .where(linkTable.URL.eq(url.toString()));

        dsl.insertInto(chatLinkTable, chatLinkTable.CHAT_ID, chatLinkTable.LINK_ID)
                .values(val(chatId), DSL.field(selectLinkIdByUrl))
                .execute();
    }

    @Override
    public void removeChatLinkByUrl(long chatId, URI url) {
        ChatLink chatLinkTable = ChatLink.CHAT_LINK;
        Link linkTable = Link.LINK;

        var selectLinkIdByUrl =
                dsl.select(linkTable.ID)
                        .from(linkTable)
                        .where(linkTable.URL.eq(url.toString()));

        dsl.deleteFrom(chatLinkTable)
                .where(chatLinkTable.CHAT_ID.eq(chatId).and(chatLinkTable.LINK_ID.in(selectLinkIdByUrl)))
                .execute();
    }

}

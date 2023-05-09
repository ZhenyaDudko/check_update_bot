package ru.tinkoff.edu.java.scrapper.domain.jooq.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.ChatLink;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import static org.jooq.impl.DSL.val;

@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository {

    private final DSLContext dsl;

    @Override
    public void addChatLink(long chatId, long linkId) {
        ChatLink chatLinkTable = ChatLink.CHAT_LINK;

        dsl.insertInto(chatLinkTable, chatLinkTable.CHAT_ID, chatLinkTable.LINK_ID)
                .values(val(chatId), val(linkId))
                .execute();
    }

    @Override
    public void removeChatLink(long chatId, long linkId) {
        ChatLink chatLinkTable = ChatLink.CHAT_LINK;

        dsl.deleteFrom(chatLinkTable)
                .where(chatLinkTable.CHAT_ID.eq(chatId).and(chatLinkTable.LINK_ID.eq(linkId)))
                .execute();
    }

    @Override
    public Long countChatByLinkId(long linkId) {
        ChatLink chatLinkTable = ChatLink.CHAT_LINK;
        return (long) dsl.fetchCount(chatLinkTable, chatLinkTable.LINK_ID.eq(linkId));
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

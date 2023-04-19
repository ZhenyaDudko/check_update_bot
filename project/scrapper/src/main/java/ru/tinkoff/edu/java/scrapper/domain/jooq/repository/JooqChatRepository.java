package ru.tinkoff.edu.java.scrapper.domain.jooq.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.jooq.data.tables.Chat;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatRepository;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {

    private final DSLContext dsl;

    @Override
    public void add(long chatId) {
        Chat chatTable = Chat.CHAT;
        dsl.insertInto(chatTable, chatTable.ID).values(chatId).execute();
    }

    @Override
    public void remove(long chatId) throws ChatNotFoundException {
        Chat chatTable = Chat.CHAT;
        dsl.delete(chatTable).where(chatTable.ID.eq(chatId)).execute();
    }

    @Override
    public Long countChatById(long chatId) {
        Chat chatTable = Chat.CHAT;
        return (long) dsl.fetchCount(chatTable, chatTable.ID.eq(chatId));
    }
}

package ru.tinkoff.edu.java.scrapper.domain.jdbc;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.ChatService;

@Component
public class JdbcChatService implements ChatService {

    private final JdbcChatRepository chatRepository;

    public JdbcChatService(JdbcChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void register(long chatId) {
        chatRepository.add(chatId);
    }

    @Override
    public void unregister(long chatId) throws ChatNotFoundException {
        chatRepository.remove(chatId);
    }
}

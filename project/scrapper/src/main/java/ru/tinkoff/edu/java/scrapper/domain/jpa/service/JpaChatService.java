package ru.tinkoff.edu.java.scrapper.domain.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.jpa.entity.Chat;
import ru.tinkoff.edu.java.scrapper.domain.jpa.repository.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.ChatService;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {

    private final JpaChatRepository jpaChatRepository;

    @Override
    @Transactional
    public void register(long chatId) {
        Chat chat = new Chat().setId(chatId);
        try {
            jpaChatRepository.saveAndFlush(chat);
        } catch (Exception ignored) {}
    }

    @Override
    @Transactional
    public void unregister(long chatId) throws ChatNotFoundException {
        jpaChatRepository.deleteById(chatId);
    }
}

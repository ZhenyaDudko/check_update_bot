package ru.tinkoff.edu.java.scrapper.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    @Override
    public void register(long chatId) {
        chatRepository.add(chatId);
    }

    @Override
    public void unregister(long chatId) {
        Long countChat = chatRepository.countChatById(chatId);
        if (countChat == null || countChat == 0) {
            throw new ChatNotFoundException("Chat not found: " + chatId);
        }
        List<Link> linkList = linkRepository.getAll(chatId);
        for (Link link : linkList) {
            try {
                linkRepository.remove(chatId, link.getUrl());
            } catch (LinkNotFoundException ignored) {}
        }
        chatRepository.remove(chatId);
    }
}

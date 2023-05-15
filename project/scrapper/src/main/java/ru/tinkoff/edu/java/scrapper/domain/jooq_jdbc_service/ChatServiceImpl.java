package ru.tinkoff.edu.java.scrapper.domain.jooq_jdbc_service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.service.ChatService;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Override
    public void register(long chatId) {
        try {
            chatRepository.add(chatId);
        } catch (Exception ignored) {
        }
    }

    @Override
    @Transactional
    public void unregister(long chatId) {
        Long countChat = chatRepository.countChatById(chatId);
        if (countChat == null || countChat == 0) {
            throw new ChatNotFoundException("Chat not found: " + chatId);
        }
        List<Link> linkList = linkRepository.getAll(chatId);
        for (Link link : linkList) {
            try {
                Long countRefs = chatLinkRepository.countChatByLinkId(link.getId());
                if (countRefs == 0) {
                    linkRepository.removeLink(chatId, link.getId());
                }
            } catch (LinkNotFoundException ignored) {
            }
        }
        chatRepository.remove(chatId);
    }
}

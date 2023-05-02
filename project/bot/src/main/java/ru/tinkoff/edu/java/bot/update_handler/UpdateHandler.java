package ru.tinkoff.edu.java.bot.update_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.bot.Bot;
import ru.tinkoff.edu.java.bot.dto.web.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
public class UpdateHandler {

    private final Bot bot;

    public void handle(LinkUpdateRequest update) {
        for (long id : update.tgChatIds()) {
            bot.sendMessage(id, update.description());
        }
    }
}

package ru.tinkoff.edu.java.bot.web.controller;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.bot.Bot;
import ru.tinkoff.edu.java.bot.dto.web.LinkUpdateRequest;

@RestController
public class Controller {

    private final Bot bot;

    public Controller(Bot bot) {
        this.bot = bot;
    }

    @PostMapping("updates/")
    public void linkUpdate(@RequestBody LinkUpdateRequest request) {
        for (long id : request.tgChatIds()) {
            bot.sendMessage(id, request.description());
        }
    }

}

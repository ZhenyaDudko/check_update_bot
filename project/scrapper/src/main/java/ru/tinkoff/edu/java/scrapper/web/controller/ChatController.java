package ru.tinkoff.edu.java.scrapper.web.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.domain.exception.ChatNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.service.ChatService;

@RestController
@RequestMapping("/tg-chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{id}")
    public void registerChat(@PathVariable("id") long id) {
        chatService.register(id);
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable("id") long id) throws ChatNotFoundException {
        chatService.unregister(id);
    }

}

package ru.tinkoff.edu.java.scrapper.web.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tg-chat")
public class ChatController {

    @PostMapping("/{id}")
    public void registerChat(@PathVariable("id") long id) {
    }

    @DeleteMapping("/{id}")
    public void deleteChar(@PathVariable("id") long id) {
    }

}

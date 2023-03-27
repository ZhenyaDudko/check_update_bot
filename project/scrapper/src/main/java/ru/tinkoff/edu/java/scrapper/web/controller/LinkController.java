package ru.tinkoff.edu.java.scrapper.web.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.web.dto.*;

@RestController
@RequestMapping("/links")
public class LinkController {

    @GetMapping
    public ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        return null;
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") long chatId, @Valid @RequestBody AddLinkRequest link) {
        return null;
    }

    @DeleteMapping
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") long chatId, @Valid @RequestBody RemoveLinkRequest link) {
        return null;
    }

}

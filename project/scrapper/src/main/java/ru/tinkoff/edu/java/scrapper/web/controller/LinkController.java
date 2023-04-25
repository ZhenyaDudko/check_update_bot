package ru.tinkoff.edu.java.scrapper.web.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.domain.exception.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkService;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/links")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    public ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        List<Link> result = linkService.listAll(chatId);
        List<LinkResponse> responseList = new ArrayList<>();
        for (Link link : result) {
            responseList.add(new LinkResponse(link.getId(), link.getUrl()));
        }
        return new ListLinksResponse(responseList, responseList.size());
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") long chatId, @Valid @RequestBody AddLinkRequest link)
            throws URISyntaxException {
        Link result = linkService.add(chatId, new URI(link.url()));
        return new LinkResponse(result.getId(), result.getUrl());
    }

    @DeleteMapping
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") long chatId, @Valid @RequestBody RemoveLinkRequest link)
            throws URISyntaxException, LinkNotFoundException {
        Link result = linkService.remove(chatId, new URI(link.url()));
        return new LinkResponse(result.getId(), result.getUrl());
    }

}

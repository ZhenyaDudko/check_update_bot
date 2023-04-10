package ru.tinkoff.edu.java.bot.dto.web;

import ru.tinkoff.edu.java.bot.dto.web.LinkResponse;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
}

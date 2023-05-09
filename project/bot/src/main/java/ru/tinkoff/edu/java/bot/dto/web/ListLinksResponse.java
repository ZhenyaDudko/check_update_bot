package ru.tinkoff.edu.java.bot.dto.web;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
}

package ru.tinkoff.edu.java.scrapper.dto.controller;

import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
}

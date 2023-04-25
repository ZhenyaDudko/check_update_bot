package ru.tinkoff.edu.java.bot.dto.web;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(long id, URI url, String description, List<Long> tgChatIds) {
}

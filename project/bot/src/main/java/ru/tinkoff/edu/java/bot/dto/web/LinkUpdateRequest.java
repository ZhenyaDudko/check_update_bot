package ru.tinkoff.edu.java.bot.dto.web;

import java.net.URI;

public record LinkUpdateRequest(long id, URI url, String description, long[] tgChatIds) {
}

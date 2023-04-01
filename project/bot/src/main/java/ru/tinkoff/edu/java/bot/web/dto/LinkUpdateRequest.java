package ru.tinkoff.edu.java.bot.web.dto;

import java.net.URI;

public record LinkUpdateRequest(long id, URI url, String description, long[] tgChatIds) {
}

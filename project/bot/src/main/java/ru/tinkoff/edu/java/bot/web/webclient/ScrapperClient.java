package ru.tinkoff.edu.java.bot.web.webclient;

import ru.tinkoff.edu.java.bot.web.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.web.dto.ListLinksResponse;

public interface ScrapperClient {

    void registerChat(long id) throws Throwable;

    void deleteChar(long id) throws Throwable;

    ListLinksResponse getLinks(long chatId) throws Throwable;

    LinkResponse addLink(long chatId, String link) throws Throwable;

    LinkResponse deleteLink(long chatId, String link) throws Throwable;
}

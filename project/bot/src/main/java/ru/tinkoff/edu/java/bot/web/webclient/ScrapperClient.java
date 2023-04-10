package ru.tinkoff.edu.java.bot.web.webclient;

import ru.tinkoff.edu.java.bot.dto.web.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.web.ListLinksResponse;

public interface ScrapperClient {

    void registerChat(long id) throws Throwable;

    void deleteChat(long id) throws Throwable;

    ListLinksResponse getLinks(long chatId) throws Throwable;

    LinkResponse addLink(long chatId, String link) throws Throwable;

    LinkResponse deleteLink(long chatId, String link) throws Throwable;
}

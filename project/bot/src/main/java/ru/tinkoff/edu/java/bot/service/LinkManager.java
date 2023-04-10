package ru.tinkoff.edu.java.bot.service;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.web.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.web.ListLinksResponse;
import ru.tinkoff.edu.java.bot.web.webclient.ScrapperClient;

@Component
public class LinkManager {

    private final ScrapperClient scrapperClient;

    public LinkManager(final ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    public void registerChat(long id) throws Throwable {
        scrapperClient.registerChat(id);
    }

    public void deleteChat(long id) throws Throwable {
        scrapperClient.deleteChat(id);
    }

    public ListLinksResponse getLinks(long chatId) throws Throwable {
        return scrapperClient.getLinks(chatId);
    }

    public LinkResponse addLink(long chatId, String link) throws Throwable {
        return scrapperClient.addLink(chatId, link);
    }

    public LinkResponse deleteLink(long chatId, String link) throws Throwable {
        return scrapperClient.deleteLink(chatId, link);
    }

}

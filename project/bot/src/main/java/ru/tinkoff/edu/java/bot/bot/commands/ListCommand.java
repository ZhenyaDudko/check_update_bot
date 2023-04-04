package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.web.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.web.exceptions.IncorrectRequestParametersException;
import ru.tinkoff.edu.java.bot.web.webclient.ScrapperClient;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListCommand extends AbstractCommand {

    private final static String COMMAND = "/list";

    private final static String DESCRIPTION = "show a list of tracked links";

    private final static String NO_LINKS_FOUND = "There are no tracked links yet";

    private final static String NOT_REGISTERED = "You are not registered. Use /start";

    private final ScrapperClient scrapperClient;

    public ListCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public String handleImpl(final Update update) {
        long chatId = update.message().chat().id();
        try {
            List<LinkResponse> links = scrapperClient.getLinks(chatId).links();
            if (links.isEmpty()) {
                return NO_LINKS_FOUND;
            } else {
                return links.stream()
                        .map(linkResponse -> linkResponse.url().toString())
                        .collect(Collectors.joining("\n"));
            }
        } catch (IncorrectRequestParametersException ignored) {
            return NOT_REGISTERED;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}

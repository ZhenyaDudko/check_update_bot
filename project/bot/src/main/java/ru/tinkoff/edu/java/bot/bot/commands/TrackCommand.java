package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.web.exceptions.IncorrectRequestParametersException;
import ru.tinkoff.edu.java.bot.web.webclient.ScrapperClient;

@Component
public class TrackCommand extends AbstractCommand {

    private final static String COMMAND = "/track";

    private final static String DESCRIPTION = "start tracking a link. Usage: /track link";

    private final static String INCORRECT_COMMAND = "Please, use this command this way: /track link";

    private final static String SUCCESS = "The link is being tracked now";

    private final static String INCORRECT_LINK = "Link is incorrect";

    private final ScrapperClient scrapperClient;

    public TrackCommand(ScrapperClient scrapperClient) {
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
        String userMessage = update.message().text();
        long chatId = update.message().chat().id();
        if (userMessage.equals(command())) {
            return INCORRECT_COMMAND;
        }
        String link = update.message().text().substring(COMMAND.length() + 1);
        try {
            scrapperClient.addLink(chatId, link);
            return SUCCESS;
        } catch (IncorrectRequestParametersException e) {
            return INCORRECT_LINK;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}

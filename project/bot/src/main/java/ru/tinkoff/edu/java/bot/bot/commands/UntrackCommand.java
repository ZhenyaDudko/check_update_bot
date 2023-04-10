package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.exceptions.web.IncorrectRequestParametersException;
import ru.tinkoff.edu.java.bot.exceptions.web.NotFoundException;
import ru.tinkoff.edu.java.bot.service.LinkManager;

@Component
public class UntrackCommand extends AbstractCommand {

    private final static String COMMAND = "/untrack";

    private final static String DESCRIPTION = "stop tracking a link. Usage: /untrack link";

    private final static String INCORRECT_COMMAND = "Please, use this command this way: /untrack link";

    private final static String SUCCESS = "The link is no longer tracked";

    private final static String INCORRECT_LINK = "Link is incorrect";

    private final static String LINK_IS_NOT_TRACKED = "Link is not tracked yet";

    private final LinkManager linkManager;

    public UntrackCommand(LinkManager linkManager) {
        this.linkManager = linkManager;
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
            linkManager.deleteLink(chatId, link);
            return SUCCESS;
        } catch (IncorrectRequestParametersException e) {
            return INCORRECT_LINK;
        } catch (NotFoundException e) {
            return LINK_IS_NOT_TRACKED;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}

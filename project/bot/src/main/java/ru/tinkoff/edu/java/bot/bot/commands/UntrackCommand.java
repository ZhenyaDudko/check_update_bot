package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.exceptions.web.IncorrectRequestParametersException;
import ru.tinkoff.edu.java.bot.exceptions.web.NotFoundException;
import ru.tinkoff.edu.java.bot.service.LinkManager;

@Component
@Slf4j
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
        String result;
        try {
            linkManager.deleteLink(chatId, link);
            result = SUCCESS;
        } catch (IncorrectRequestParametersException e) {
            result = INCORRECT_LINK;
        } catch (NotFoundException e) {
            result = LINK_IS_NOT_TRACKED;
        } catch (Throwable e) {
            log.error(Arrays.toString(e.getStackTrace()));
            result = null;
        }
        return result;
    }
}

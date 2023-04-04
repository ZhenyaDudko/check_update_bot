package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.commands.*;

import java.util.List;

@Component
public class UserMessageProcessor {

    private static final String UNKNOWN_COMMAND = "Unknown command. Type /help to see available commands";

    private final List<? extends Command> commands;

    public UserMessageProcessor(final List<? extends Command> commands) {
        this.commands = commands;
    }

    public String process(final Update update) {
        if (update.message() == null || update.message().text() == null) {
            return null;
        }
        String result = null;
        for (Command command: commands) {
            if (command.supports(update)) {
                result = command.handle(update);
            }
        }
        if (result == null) {
            return UNKNOWN_COMMAND;
        }
        return result;
    }

    public List<? extends Command> commands() {
        return commands;
    }

}
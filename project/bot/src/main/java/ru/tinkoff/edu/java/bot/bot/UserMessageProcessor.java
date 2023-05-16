package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.commands.Command;
import ru.tinkoff.edu.java.bot.metric.MessageMetric;

@Component
public class UserMessageProcessor {

    private static final String UNKNOWN_COMMAND = "Unknown command. Type /help to see available commands";

    private final List<Command> commands;

    public UserMessageProcessor(final List<Command> commands) {
        this.commands = commands;
    }

    public String process(final Update update) {
        if (update.message() == null || update.message().text() == null) {
            return null;
        }
        MessageMetric.incrementMessageCount();
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

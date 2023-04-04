package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelpCommand extends AbstractCommand {

    private final static String COMMAND = "/help";

    private final static String DESCRIPTION = "show available commands";

    private final static String ABOUT_BOT = "I can track your links and notify you when their content is updated.";

    private final String commands;

    public HelpCommand(final List<? extends Command> commands) {
        StringBuilder builder = new StringBuilder();
        for (Command command : commands) {
            builder.append(command.command())
                    .append(" - ")
                    .append(command.description())
                    .append("\n");
        }
        this.commands = builder.toString();
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
        return ABOUT_BOT + "\n\n" + commands;
    }

}
package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends AbstractCommand {

    private final static String COMMAND = "/help";

    private final static String DESCRIPTION = "show available commands";

    private final static String ABOUT_BOT = """
            I can track your links and notify you when their content is updated. \n
            Now I support links to github repositories and stackoverflow questions""";

    private final String commands;

    public HelpCommand(final List<Command> commands) {
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

package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;

public interface Command {

    String command();

    String description();

    String handle(final Update update);

    default boolean supports(final Update update) {
        return update.message().text().startsWith(command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}

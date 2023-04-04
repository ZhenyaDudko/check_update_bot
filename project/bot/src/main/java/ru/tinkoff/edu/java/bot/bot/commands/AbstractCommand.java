package ru.tinkoff.edu.java.bot.bot.commands;

import com.pengrad.telegrambot.model.Update;

public abstract class AbstractCommand implements Command {

    @Override
    public String handle(final Update update) {
        if (supports(update)) {
            return handleImpl(update);
        }
        return null;
    }

    protected abstract String handleImpl(final Update update);
}

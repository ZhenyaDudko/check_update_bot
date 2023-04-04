package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.commands.Command;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

import java.util.List;

@Component
public class Bot {

    private final TelegramBot bot;

    private final UserMessageProcessor userMessageProcessor;

    public Bot(final ApplicationConfig applicationConfig, final UserMessageProcessor userMessageProcessor) {
        this.bot = new TelegramBot(applicationConfig.token());
        this.userMessageProcessor = userMessageProcessor;
        start();
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> BaseResponse execute(final BaseRequest<T, R> request) {
        return bot.execute(request);
    }

    public void start() {
        List<BotCommand> commands = userMessageProcessor.commands().stream().map(Command::toApiCommand).toList();
        bot.execute(new SetMyCommands(commands.toArray(new BotCommand[0])));
        bot.setUpdatesListener(list -> {
            for (final Update update : list) {
                String response = userMessageProcessor.process(update);
                if (response != null) {
                    bot.execute(new SendMessage(update.message().chat().id(), response));
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void stop() {
        bot.removeGetUpdatesListener();
    }

}

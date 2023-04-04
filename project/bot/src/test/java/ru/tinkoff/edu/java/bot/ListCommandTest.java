package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.bot.commands.ListCommand;
import ru.tinkoff.edu.java.bot.web.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.web.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.web.webclient.ScrapperClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {

    @InjectMocks
    ListCommand listCommand;

    @Mock
    ScrapperClient scrapperClient;

    @Test
    void handle_shouldReturnSpecialMessage() throws Throwable {
        //given
        Update update = mockUpdate();
        Mockito.when(scrapperClient.getLinks(1L))
                .thenReturn(new ListLinksResponse(new ArrayList<>(), 0));

        //when
        String result = listCommand.handle(update);

        //then
        assertThat(result).isEqualTo("There are no tracked links yet");
    }

    @Test
    void handle_shouldReturnLinks() throws Throwable {
        //given
        Update update = mockUpdate();
        URI link1 = URI.create("https://dog.com");
        URI link2 = URI.create("https://cat.com");
        Mockito.when(scrapperClient.getLinks(1L))
                .thenReturn(
                        new ListLinksResponse(List.of(
                                new LinkResponse(1L, link1),
                                new LinkResponse(2L, link2)
                        ), 0));

        //when
        String result = listCommand.handle(update);

        //then
        assertThat(result).isEqualTo(link1 + "\n" + link2);
    }

    Update mockUpdate() {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(1L);
        return update;
    }
}

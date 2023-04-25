import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
public class JdbcChatLinkTest extends JdbcBaseTest {

    @Autowired
    private ChatLinkRepository chatLinkRepository;

    private static final String COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY =
            "select count(*) from chat_link where chat_id = ? and link_id = ?";

    public JdbcChatLinkTest() throws Exception {
        super();
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void addChatLinkByUrl_shouldAdd() {
        // given
        long chatId = 1;
        insertChat(chatId);
        long linkId = 1;
        URI url = new URI("https://abacaba");
        insertLink(linkId, url);

        // when
        chatLinkRepository.addChatLink(chatId, linkId);

        // then
        Long count = countChatLinkByIds(chatId, linkId);

        assertThat(count).isEqualTo(1);
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void removeChatLinkByUrl_shouldRemove() {
        // given
        long chatId = 1;
        insertChat(chatId);
        long linkId = 1;
        URI url = new URI("https://abacaba");
        insertLink(linkId, url);
        insertChatLink(chatId, linkId);

        // when
        chatLinkRepository.removeChatLink(chatId, linkId);

        // then
        Long count = countChatLinkByIds(chatId, linkId);

        assertThat(count).isEqualTo(0);
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void getLinkUsersTest() {
        // given
        long chat1Id = 1;
        long chat2Id = 2;
        long linkId = 1;
        URI url1 = new URI("https://abc");

        insertChat(chat1Id);
        insertChat(chat2Id);
        insertLink(linkId, url1);
        insertChatLink(chat1Id, linkId);
        insertChatLink(chat2Id, linkId);

        // when
        List<Chat> result = chatLinkRepository.getChatsByLinkId(linkId);

        // then
        assertAll("Assert result",
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getId()).isIn(chat1Id, chat2Id),
                () -> assertThat(result.get(1).getId()).isIn(chat1Id, chat2Id)
        );
    }

    @Transactional
    Long countChatLinkByIds(long chatId, long linkId) {
        return jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY, Long.class, chatId, linkId);
    }

}

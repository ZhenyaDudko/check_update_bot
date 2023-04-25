import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

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
        chatLinkRepository.addChatLinkByUrl(chatId, url);

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
        chatLinkRepository.removeChatLinkByUrl(chatId, url);

        // then
        Long count = countChatLinkByIds(chatId, linkId);

        assertThat(count).isEqualTo(0);
    }

    @Transactional
    Long countChatLinkByIds(long chatId, long linkId) {
        return jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY, Long.class, chatId, linkId);
    }

}

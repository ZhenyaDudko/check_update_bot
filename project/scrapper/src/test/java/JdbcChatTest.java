import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatRepository;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
public class JdbcChatTest extends JdbcBaseTest {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatRepository linkRepository;

    private static final String COUNT_CHAT_BY_ID_QUERY =
            "select count(*) from chat where id = ?";

    private static final String COUNT_CHAT_LINK_BY_CHAT_ID_QUERY =
            "select count(*) from chat_link where chat_id = ?";

    private static final String COUNT_LINK_BY_ID_QUERY =
            "select count(*) from link where id = ?";

    public JdbcChatTest() throws Exception {
        super();
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void addTest() {
        // given
        long chatId = 1;

        // when
        chatRepository.add(chatId);

        // then
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_BY_ID_QUERY, Long.class, chatId);
        assertThat(count).isEqualTo(1);
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void removeTest() {
        // given
        long chatId = 1;
        long linkId = 1;
        URI url = new URI("https://abc.abc.abc");
        insertChat(chatId);
        insertLink(linkId, url);
        insertChatLink(chatId, linkId);

        // when
        chatRepository.remove(chatId);

        // then
        Long countRowsInChatLink = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_QUERY, Long.class, chatId);

        Long countRowsInChat = jdbcTemplate.queryForObject(COUNT_CHAT_BY_ID_QUERY, Long.class, chatId);

        Long countRowsInLink = jdbcTemplate.queryForObject(COUNT_LINK_BY_ID_QUERY, Long.class, linkId);

        assertAll("Assert rows",
                () -> assertThat(countRowsInChatLink).isEqualTo(0),
                () -> assertThat(countRowsInChat).isEqualTo(0),
                () -> assertThat(countRowsInLink).isEqualTo(1)
        );
    }

}

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.repository.JdbcChatRepository;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
public class JdbcChatTest extends IntegrationEnvironment{

    @Autowired
    private JdbcChatRepository chatRepository;

    @Autowired
    private JdbcChatRepository linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        Long count = jdbcTemplate.queryForObject("select count(*) from chat where id = ?", Long.class, chatId);
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
        Long countRowsInChatLink = jdbcTemplate.queryForObject("select count(*) from chat_link " +
                "where chat_id = ?", Long.class, chatId);

        Long countRowsInChat = jdbcTemplate.queryForObject("select count(*) from chat " +
                "where id = ?", Long.class, chatId);

        Long countRowsInLink = jdbcTemplate.queryForObject("select count(*) from link " +
                "where id = ?", Long.class, linkId);

        assertAll("Assert rows",
                () -> assertThat(countRowsInChatLink).isEqualTo(0),
                () -> assertThat(countRowsInChat).isEqualTo(0),
                () -> assertThat(countRowsInLink).isEqualTo(0)
        );
    }

    @Transactional
    void insertChat(long id) {
        jdbcTemplate.update("insert into chat (id) values (?)", id);
    }

    @Transactional
    void insertLink(long id, URI url) {
        jdbcTemplate.update("insert into link (id, url) overriding system value values (?, ?)", id, url.toString());
    }

    @Transactional
    void insertChatLink(long chatId, long linkId) {
        jdbcTemplate.update("insert into chat_link (chat_id, link_id) values (?, ?)", chatId, linkId);
    }

}

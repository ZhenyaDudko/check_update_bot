import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
public class JdbcLinkTest extends IntegrationEnvironment {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String COUNT_CHAT_LINK_BY_CHAT_ID_URL_QUERY = """
            select count(*) from chat_link cl join link on cl.link_id = link.id
            where cl.chat_id = ? and link.url = ?
            """;

    private static final String COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY =
            "select count(*) from chat_link where chat_id = ? and link_id = ?";

    private static final String COUNT_LINK_BY_URL_QUERY = "select count(*) from link where url = ?";

    public JdbcLinkTest() throws Exception {
        super();
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void addTest() {
        // given
        long chatId = 1;
        insertChat(chatId);
        URI url = new URI("https://abacaba");

        // when
        linkRepository.add(chatId, url);

        // then
        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_URL_QUERY,
                Long.class, chatId, url.toString());

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
        linkRepository.remove(chatId, url);

        // then
        Long countRowsInChatLink = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY,
                Long.class, chatId, linkId);

        Long countRowsInLink = jdbcTemplate.queryForObject(COUNT_LINK_BY_URL_QUERY, Long.class, url.toString());

        assertAll("Assert rows",
                () -> assertThat(countRowsInChatLink).isEqualTo(0),
                () -> assertThat(countRowsInLink).isEqualTo(0)
        );
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void getAllTest() {
        // given
        long chatId = 1;
        long link1Id = 1;
        long link2Id = 2;
        URI url1 = new URI("https://abc");
        URI url2 = new URI("https://def");

        insertChat(chatId);
        insertLink(link1Id, url1);
        insertLink(link2Id, url2);
        insertChatLink(chatId, link1Id);
        insertChatLink(chatId, link2Id);

        // when
        List<Link> result = linkRepository.getAll(chatId);

        // then
        assertAll("Assert result",
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getUrl()).isIn(url1, url2),
                () -> assertThat(result.get(1).getUrl()).isIn(url1, url2)
        );
    }

    @Transactional
    void insertChat(long id) {
        jdbcTemplate.update(JdbcBaseTest.INSERT_CHAT_QUERY, id);
    }

    @Transactional
    void insertLink(long id, URI url) {
        jdbcTemplate.update(JdbcBaseTest.INSERT_LINK_QUERY, id, url.toString());
    }

    @Transactional
    void insertChatLink(long chatId, long linkId) {
        jdbcTemplate.update(JdbcBaseTest.INSERT_CHAT_LINK_QUERY, chatId, linkId);
    }

}

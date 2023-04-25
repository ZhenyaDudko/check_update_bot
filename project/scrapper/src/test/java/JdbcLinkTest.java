import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class JdbcLinkTest extends JdbcBaseTest {

    @Autowired
    private LinkRepository linkRepository;

    private static final String COUNT_LINK_BY_URL_QUERY = "select count(*) from link where url = ?";

    public JdbcLinkTest() throws Exception {
        super();
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void addLinkIfNotExists_shouldAddNew() {
        // given
        long chatId = 1;
        insertChat(chatId);
        URI url = new URI("https://abacaba");

        // when
        linkRepository.addLinkIfNotExists(chatId, url);

        // then
        Long count = countLinkByUrl(url);

        assertThat(count).isEqualTo(1);
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void addLinkIfNotExists_shouldNotAdd() {
        // given
        long chatId = 1;
        long linkId = 1;
        insertChat(chatId);
        URI url = new URI("https://abacaba");
        insertLink(linkId, url);

        // when
        linkRepository.addLinkIfNotExists(chatId, url);

        // then
        Long count = countLinkByUrl(url);

        assertThat(count).isEqualTo(1);
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void removeLinkIfNoOneRefers_shouldRemove() {
        // given
        long chatId = 1;
        long linkId = 1;
        URI url = new URI("https://abc.abc.abc");
        insertChat(chatId);
        insertLink(linkId, url);

        // when
        linkRepository.removeLinkIfNoOneRefers(chatId, url);

        // then
        Long count = countLinkByUrl(url);

        assertThat(count).isEqualTo(0);
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void removeLinkIfNoOneRefers_shouldNotRemove() {
        // given
        long chatId = 1;
        long linkId = 1;
        URI url = new URI("https://abc.abc.abc");
        insertChat(chatId);
        insertLink(linkId, url);
        insertChatLink(chatId, linkId);

        // when
        linkRepository.removeLinkIfNoOneRefers(chatId, url);

        // then
        Long count = countLinkByUrl(url);

        assertThat(count).isEqualTo(1);
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void getAll_shouldReturnAll() {
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
    Long countLinkByUrl(URI url) {
        return jdbcTemplate.queryForObject(COUNT_LINK_BY_URL_QUERY, Long.class, url.toString());
    }

}

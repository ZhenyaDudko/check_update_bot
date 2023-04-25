import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRowMapper;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
public class JdbcLinkTest extends JdbcBaseTest {

    @Autowired
    private LinkRepository linkRepository;

    private static final String COUNT_LINK_BY_URL_QUERY = "select count(*) from link where url = ?";

    private static final String SELECT_LINK_BY_ID_QUERY = "select * from link where id = ?";

    private static final String INSERT_LINK_WITH_TIME_QUERY =
            "insert into link (id, url, last_update) overriding system value values (?, ?, ?)";

    public JdbcLinkTest() throws Exception {
        super();
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void addLink_shouldAddNew() {
        // given
        long chatId = 1;
        insertChat(chatId);
        URI url = new URI("https://abacaba");

        // when
        linkRepository.addLink(chatId, url);

        // then
        Long count = countLinkByUrl(url);

        assertThat(count).isEqualTo(1);
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void removeLink_shouldRemove() {
        // given
        long chatId = 1;
        long linkId = 1;
        URI url = new URI("https://abc.abc.abc");
        insertChat(chatId);
        insertLink(linkId, url);

        // when
        linkRepository.removeLink(chatId, linkId);

        // then
        Long count = countLinkByUrl(url);

        assertThat(count).isEqualTo(0);
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

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void updateTimeTest() {
        // given
        OffsetDateTime time = OffsetDateTime.now();
        long linkId = 1;
        URI url = new URI("https://abacaba");
        insertLink(linkId, url);

        // when
        linkRepository.updateTimeByLinkId(linkId, time);

        // then
        List<Link> links = jdbcTemplate.query(SELECT_LINK_BY_ID_QUERY, new LinkRowMapper(), linkId);

        assertAll("Assert update time of link results",
                () -> assertThat(links.size()).isEqualTo(1),
                () -> assertThat(links.get(0).getId()).isEqualTo(linkId),
                () -> assertThat(links.get(0).getLastUpdate()).isEqualTo(time)
        );
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void getLastUpdatedTest() {
        // given
        long link1Id = 1;
        long link2Id = 2;
        URI url1 = new URI("https://abc");
        URI url2 = new URI("https://def");
        OffsetDateTime time1 = OffsetDateTime.now();
        OffsetDateTime time2 = time1.minusHours(2);
        insertLinkWithTime(link1Id, url1, time1);
        insertLinkWithTime(link2Id, url2, time2);

        // when
        List<Link> resultList = linkRepository.getLongAgoUpdated();

        // then
        assertAll("Assert get last updated links results",
                () -> assertThat(resultList.size()).isEqualTo(1),
                () -> assertThat(resultList.get(0).getId()).isEqualTo(2)
        );
    }

    @Transactional
    Long countLinkByUrl(URI url) {
        return jdbcTemplate.queryForObject(COUNT_LINK_BY_URL_QUERY, Long.class, url.toString());
    }

    @Transactional
    void insertLinkWithTime(long id, URI url, OffsetDateTime time) {
        jdbcTemplate.update(INSERT_LINK_WITH_TIME_QUERY, id, url.toString(), time);
    }

}

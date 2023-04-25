import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkUpdatesRepository;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRowMapper;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
@Slf4j
public class JdbcLinkUpdatesTest extends IntegrationEnvironment{

    @Autowired
    private LinkUpdatesRepository linkUpdatesRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_LINK_WITH_TIME_QUERY =
            "insert into link (id, url, last_update) overriding system value values (?, ?, ?)";

    private static final String SELECT_LINK_BY_ID_QUERY =
            "select * from link where id = ?";


    public JdbcLinkUpdatesTest() throws Exception {
        super();
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
        linkUpdatesRepository.updateTimeByLinkId(linkId, time);

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
        List<Link> resultList = linkUpdatesRepository.getLongAgoUpdated();

        // then
        assertAll("Assert get last updated links results",
                () -> assertThat(resultList.size()).isEqualTo(1),
                () -> assertThat(resultList.get(0).getId()).isEqualTo(2)
        );
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
        List<Chat> result = linkUpdatesRepository.getChatsByLinkId(linkId);

        // then
        assertAll("Assert result",
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getId()).isIn(chat1Id, chat2Id),
                () -> assertThat(result.get(1).getId()).isIn(chat1Id, chat2Id)
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

    @Transactional
    void insertLinkWithTime(long id, URI url, OffsetDateTime time) {
        jdbcTemplate.update(INSERT_LINK_WITH_TIME_QUERY, id, url.toString(), time);
    }

}

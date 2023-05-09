import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.dto.domain.Chat;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;
import ru.tinkoff.edu.java.scrapper.dto.domain.LinkRowMapper;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
public class LinkUpdaterTest extends JdbcBaseTest {

    public LinkUpdaterTest() throws Exception {
        super();
    }

    @Autowired
    private LinkUpdater linkUpdaterService;

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void updateTime_shouldUpdate() {
        // given
        OffsetDateTime time = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        long linkId = 1;
        URI url = new URI("https://abacaba");
        insertLink(linkId, url);

        // when
        linkUpdaterService.updateTimeByLinkId(linkId, time);

        // then
        List<Link> links = jdbcTemplate.query(SELECT_LINK_BY_ID_QUERY, new LinkRowMapper(), linkId);

        assertAll("Assert update time of link results",
                () -> assertThat(links.size()).isEqualTo(1),
                () -> assertThat(links.get(0).getId()).isEqualTo(linkId),
                () -> assertThat(links.get(0).getLastUpdate().truncatedTo(ChronoUnit.SECONDS))
                        .isEqualTo(time)
        );
    }

    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    void getLongAgoUpdated_shouldGet() {
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
        List<Link> resultList = linkUpdaterService.getLongAgoUpdated();

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
    void getChatsByLinkId_shouldGet() {
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
        List<Chat> result = linkUpdaterService.getChatsByLinkId(linkId);

        // then
        assertAll("Assert result",
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getId()).isIn(chat1Id, chat2Id),
                () -> assertThat(result.get(1).getId()).isIn(chat1Id, chat2Id)
        );
    }
}

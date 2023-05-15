import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.service.LinkService;
import ru.tinkoff.edu.java.scrapper.dto.domain.Link;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = ScrapperApplication.class)
public class LinkServiceTest extends JdbcBaseTest {

    @Autowired
    private LinkService linkService;

    public LinkServiceTest() throws Exception {
        super();
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldAdd() {
        insertChat(1);
        URI url = URI.create("https://github.com/ZhenyaDudko/tinkoff_project");

        Link link = linkService.add(1, url);

        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY,
                Long.class, 1, link.getId());
        assertThat(count).isEqualTo(1);
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldNotAddLinkButRelation() {
        insertChat(1);
        URI url = URI.create("https://github.com/ZhenyaDudko/tinkoff_project");
        insertLink(1, url);

        linkService.add(1, url);

        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY,
                Long.class, 1, 1);
        assertThat(count).isEqualTo(1);
    }

    @Transactional
    @Rollback
    @Test
    void addLinksAndList_shouldList() {
        insertChat(1);
        URI url1 = URI.create("https://github.com/hello/cat");
        URI url2 = URI.create("https://github.com/abacaba/abc");

        linkService.add(1, url1);
        linkService.add(1, url2);
        List<Link> res = linkService.listAll(1);

        assertAll("Assert results",
                () -> assertThat(res.size()).isEqualTo(2),
                () -> assertThat(res.get(0).getUrl().toString())
                        .isNotEqualTo(res.get(1).getUrl().toString())
        );
    }

    @Transactional
    @Rollback
    @Test
    void remove_shouldRemoveRelationButNotLink() {
        insertChat(1);
        insertChat(2);
        URI url = URI.create("https://github.com/abacaba/abc");
        insertLink(1, url);
        insertChatLink(1, 1);
        insertChatLink(2, 1);

        linkService.remove(1, url);

        Long chat1Count = jdbcTemplate.queryForObject(COUNT_CHAT_BY_ID_QUERY, Long.class, 1);
        Long chat2Count = jdbcTemplate.queryForObject(COUNT_CHAT_BY_ID_QUERY, Long.class, 2);
        Long linkCount = jdbcTemplate.queryForObject(COUNT_LINK_BY_ID_QUERY, Long.class, 1);
        Long chat1LinkCount = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY, Long.class, 1, 1);
        Long chat2LinkCount = jdbcTemplate.queryForObject(COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY, Long.class, 2, 1);


        assertAll("Assert results",
                () -> assertThat(chat1Count).isEqualTo(1),
                () -> assertThat(chat2Count).isEqualTo(1),
                () -> assertThat(linkCount).isEqualTo(1),
                () -> assertThat(chat1LinkCount).isEqualTo(0),
                () -> assertThat(chat2LinkCount).isEqualTo(1)
        );
    }

}

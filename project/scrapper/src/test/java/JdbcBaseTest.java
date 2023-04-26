import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;

import java.net.URI;
import java.time.OffsetDateTime;

@SpringBootTest(classes = ScrapperApplication.class)
public class JdbcBaseTest extends IntegrationEnvironment {

    public static final String INSERT_CHAT_QUERY =
            "insert into chat (id) values (?)";

    public static final String INSERT_LINK_QUERY =
            "insert into link (id, url) overriding system value values (?, ?)";

    public static final String INSERT_CHAT_LINK_QUERY =
            "insert into chat_link (chat_id, link_id) values (?, ?)";

    public static final String COUNT_CHAT_BY_ID_QUERY =
            "select count(*) from chat where id = ?";

    public static final String COUNT_CHAT_LINK_BY_CHAT_ID_QUERY =
            "select count(*) from chat_link where chat_id = ?";

    public static final String COUNT_CHAT_LINK_BY_CHAT_ID_LINK_ID_QUERY =
            "select count(*) from chat_link where chat_id = ? and link_id = ?";

    public static final String COUNT_LINK_BY_ID_QUERY =
            "select count(*) from link where id = ?";

    public static final String COUNT_LINK_BY_URL_QUERY = "select count(*) from link where url = ?";

    public static final String SELECT_LINK_BY_ID_QUERY = "select * from link where id = ?";

    public static final String INSERT_LINK_WITH_TIME_QUERY =
            "insert into link (id, url, last_update) overriding system value values (?, ?, ?)";

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public JdbcBaseTest() throws Exception {
        super();
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
    Long countLinkByUrl(URI url) {
        return jdbcTemplate.queryForObject(COUNT_LINK_BY_URL_QUERY, Long.class, url.toString());
    }

    @Transactional
    void insertLinkWithTime(long id, URI url, OffsetDateTime time) {
        jdbcTemplate.update(INSERT_LINK_WITH_TIME_QUERY, id, url.toString(), time);
    }
}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;

import java.net.URI;

@SpringBootTest(classes = ScrapperApplication.class)
public class JdbcBaseTest extends IntegrationEnvironment {

    public static final String INSERT_CHAT_QUERY =
            "insert into chat (id) values (?)";

    public static final String INSERT_LINK_QUERY =
            "insert into link (id, url) overriding system value values (?, ?)";

    public static final String INSERT_CHAT_LINK_QUERY =
            "insert into chat_link (chat_id, link_id) values (?, ?)";

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
}

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.domain.service.ChatService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScrapperApplication.class)
public class ChatServiceTest extends JdbcBaseTest {

    @Autowired
    private ChatService chatService;

    public ChatServiceTest() throws Exception {
        super();
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldAdd() {
        chatService.register(1);

        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_BY_ID_QUERY, Long.class, 1);
        assertThat(count).isEqualTo(1);
    }

    @Transactional
    @Rollback
    @Test
    void add_shouldIgnore() {
        insertChat(1);

        chatService.register(1);

        Long count = jdbcTemplate.queryForObject(COUNT_CHAT_BY_ID_QUERY, Long.class, 1);
        assertThat(count).isEqualTo(1);
    }


}

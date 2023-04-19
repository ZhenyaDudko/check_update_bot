public class JdbcBaseTest {

    public static final String INSERT_CHAT_QUERY =
            "insert into chat (id) values (?)";

    public static final String INSERT_LINK_QUERY =
            "insert into link (id, url) overriding system value values (?, ?)";

    public static final String INSERT_CHAT_LINK_QUERY =
            "insert into chat_link (chat_id, link_id) values (?, ?)";
}

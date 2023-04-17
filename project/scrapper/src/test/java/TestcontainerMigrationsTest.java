import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers
public class TestcontainerMigrationsTest extends IntegrationEnvironment {

    public TestcontainerMigrationsTest() throws Exception {
    }

    @Test
    void db_shouldContainChatAndLinkTables() throws SQLException {
        ResultSet resultSet = getContainerConnection().createStatement().executeQuery(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'"
        );

        boolean hasChatTable = false;
        boolean hasLinkTable = false;
        boolean hasChatLinkTable = false;
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            switch (tableName) {
                case "chat" -> hasChatTable = true;
                case "link" -> hasLinkTable = true;
                case "chat_link" -> hasChatLinkTable = true;
            }
        }

        boolean finalHasChatTable = hasChatTable;
        boolean finalHasLinkTable = hasLinkTable;
        boolean finalHasChatLinkTable = hasChatLinkTable;

        assertAll("Assert tables",
                () -> assertThat(finalHasChatTable).isTrue(),
                () -> assertThat(finalHasLinkTable).isTrue(),
                () -> assertThat(finalHasChatLinkTable).isTrue()
        );
    }
}

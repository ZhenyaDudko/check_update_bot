import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers
public class TestcontainerMigrationsTest extends IntegrationEnvironment {

    static Connection getContainerConnection() throws SQLException {
        return DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword()
        );
    }

    @BeforeAll
    static void init() throws Exception {
        Database database = DatabaseFactory
                .getInstance().findCorrectDatabaseImplementation(new JdbcConnection(getContainerConnection()));
        Path migrations = Path.of(".").toAbsolutePath().getParent().getParent().resolve("migrations");
        Liquibase liquibase = new liquibase.Liquibase("master.xml",
                new DirectoryResourceAccessor(migrations), database);
        liquibase.update(new Contexts(), new LabelExpression());
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

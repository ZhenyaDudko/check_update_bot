import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class IntegrationEnvironment {

    static final JdbcDatabaseContainer<?> DB_CONTAINER;

    static {
        DB_CONTAINER = new PostgreSQLContainer<>("postgres:15");
        DB_CONTAINER.start();
    }

    public IntegrationEnvironment() throws Exception {
        Database database = DatabaseFactory
                .getInstance().findCorrectDatabaseImplementation(new JdbcConnection(getContainerConnection()));
        Path migrations = Path.of(".").toAbsolutePath().getParent().getParent().resolve("migrations");
        Liquibase liquibase = new liquibase.Liquibase("master.xml",
                new DirectoryResourceAccessor(migrations), database);
        liquibase.update(new Contexts(), new LabelExpression());
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
        registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
    }

    static Connection getContainerConnection() throws SQLException {
        return DriverManager.getConnection(
                DB_CONTAINER.getJdbcUrl(),
                DB_CONTAINER.getUsername(),
                DB_CONTAINER.getPassword()
        );
    }
}

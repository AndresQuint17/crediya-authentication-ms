package co.com.myproject.r2dbc.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.*;

class PostgresqlConnectionPropertiesTest {
    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(TestConfig.class)
                    .withPropertyValues(
                            "adapters.r2dbc.host=localhost",
                            "adapters.r2dbc.port=5432",
                            "adapters.r2dbc.database=mydb",
                            "adapters.r2dbc.schema=public",
                            "adapters.r2dbc.username=myuser",
                            "adapters.r2dbc.password=secret"
                    );

    @EnableConfigurationProperties(PostgresqlConnectionProperties.class)
    static class TestConfig {
    }

    @Test
    void shouldBindPropertiesCorrectly() {
        contextRunner.run(context -> {
            PostgresqlConnectionProperties props = context.getBean(PostgresqlConnectionProperties.class);

            assertNotNull(props);
            assertEquals("localhost", props.host());
            assertEquals(5432, props.port());
            assertEquals("mydb", props.database());
            assertEquals("public", props.schema());
            assertEquals("myuser", props.username());
            assertEquals("secret", props.password());
        });
    }
}
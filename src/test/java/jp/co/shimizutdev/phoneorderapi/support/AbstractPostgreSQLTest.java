package jp.co.shimizutdev.phoneorderapi.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * PostgreSQLテスト共通設定
 */
public abstract class AbstractPostgreSQLTest {

    /**
     * PostgreSQLコンテナ
     */
    @SuppressWarnings("resource")
    protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
        new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("phone_order_api_test_db")
            .withUsername("test")
            .withPassword("test");

    static {
        POSTGRESQL_CONTAINER.start();
    }

    /**
     * テスト用データソースを設定する。
     *
     * @param registry DynamicPropertyRegistry
     */
    @DynamicPropertySource
    static void registerDataSourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRESQL_CONTAINER::getDriverClassName);
    }
}

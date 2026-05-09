package jp.co.shimizutdev.phoneorderapi.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/** PostgreSQLテスト共通設定 */
public abstract class AbstractPostgreSQLTest {

  private static final String TEST_PROPERTIES_PATH = "application.properties";
  private static final String DATASOURCE_USERNAME_KEY = "spring.datasource.username";
  private static final String DATASOURCE_PASSWORD_KEY = "spring.datasource.password";
  private static final Properties TEST_PROPERTIES = loadTestProperties();

  /** PostgreSQLコンテナ */
  @SuppressWarnings("resource")
  protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
      new PostgreSQLContainer<>("postgres:16")
          .withDatabaseName("phone_order_api_test_db")
          .withUsername(resolveRequiredProperty(DATASOURCE_USERNAME_KEY))
          .withPassword(resolveRequiredProperty(DATASOURCE_PASSWORD_KEY));

  static {
    POSTGRESQL_CONTAINER.start();
  }

  /**
   * テスト用データソースを設定する
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

  private static Properties loadTestProperties() {
    try (InputStream input =
        AbstractPostgreSQLTest.class.getClassLoader().getResourceAsStream(TEST_PROPERTIES_PATH)) {
      if (input == null) {
        throw new IllegalStateException(TEST_PROPERTIES_PATH + " がクラスパス上に見つかりません。");
      }
      Properties properties = new Properties();
      properties.load(input);
      return properties;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static String resolveRequiredProperty(final String key) {
    String value = TEST_PROPERTIES.getProperty(key);
    if (value == null || value.isBlank()) {
      throw new IllegalStateException(key + " が " + TEST_PROPERTIES_PATH + " に設定されていません。");
    }
    return value;
  }
}

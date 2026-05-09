package jp.co.shimizutdev.phoneorderapi.presentation.security;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/** Security の統合テスト */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest extends AbstractPostgreSQLTest {

  /** MockMvc */
  @Autowired private MockMvc mockMvc;

  /** Basic認証ユーザー名 */
  @Value("${app.security.basic.username}")
  private String basicUsername;

  /** Basic認証パスワード */
  @Value("${app.security.basic.password}")
  private String basicPassword;

  /**
   *
   *
   * <pre>
   * Given 認証情報を付与しないリクエストを送信する
   * When  保護対象APIにアクセスする
   * Then  401 Unauthorized が返却される
   * </pre>
   *
   * @throws Exception テスト実行中に例外が発生した場合
   */
  @Test
  @DisplayName("認証情報なしアクセスで401を返すこと")
  void shouldReturnUnauthorizedWhenNoCredentialsProvided() throws Exception {
    mockMvc
        .perform(get("/api/v1/orders"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
        .andExpect(jsonPath("$.message").value("認証が必要です。"))
        .andExpect(jsonPath("$.path").value("/api/v1/orders"))
        .andExpect(jsonPath("$.validationErrors", hasSize(1)))
        .andExpect(jsonPath("$.validationErrors[0].field").value("auth"));
  }

  /**
   *
   *
   * <pre>
   * Given 有効なBasic認証情報を付与したリクエストを送信する
   * When  保護対象APIにアクセスする
   * Then  200 OK が返却される
   * </pre>
   *
   * @throws Exception テスト実行中に例外が発生した場合
   */
  @Test
  @DisplayName("有効な認証情報で200を返すこと")
  void shouldReturnOkWhenValidCredentialsProvided() throws Exception {
    mockMvc
        .perform(get("/api/v1/orders").with(httpBasic(basicUsername, basicPassword)))
        .andExpect(status().isOk());
  }

  /**
   *
   *
   * <pre>
   * Given 認証済みユーザーで非許可エンドポイントにアクセスする
   * When  アクセス拒否対象APIにアクセスする
   * Then  403 Forbidden が返却される
   * </pre>
   *
   * @throws Exception テスト実行中に例外が発生した場合
   */
  @Test
  @DisplayName("認可エラー時に403を返すこと")
  void shouldReturnForbiddenWhenAccessingDeniedEndpoint() throws Exception {
    mockMvc
        .perform(get("/api/v1/admin").with(httpBasic(basicUsername, basicPassword)))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403))
        .andExpect(jsonPath("$.error").value("FORBIDDEN"))
        .andExpect(jsonPath("$.message").value("アクセスが拒否されました。"))
        .andExpect(jsonPath("$.path").value("/api/v1/admin"))
        .andExpect(jsonPath("$.validationErrors", hasSize(1)))
        .andExpect(jsonPath("$.validationErrors[0].field").value("auth"));
  }
}

package jp.co.shimizutdev.phoneorderapi.presentation.log;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.shimizutdev.phoneorderapi.infrastructure.log.LogMasker;
import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
import jp.co.shimizutdev.phoneorderapi.support.ResetLogLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

/** リクエストレスポンスログのフィルターテスト */
@SpringBootTest(
    properties =
        "logging.level.jp.co.shimizutdev.phoneorderapi.presentation.log."
            + "RequestResponseLogFilter=DEBUG")
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
@ResetLogLevel(RequestResponseLogFilter.class)
class RequestResponseLogFilterTest extends AbstractPostgreSQLTest {

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
   * Given マスク対象ヘッダーとマスク対象項目を含むリクエストを送信する
   * When  注文登録APIを実行する
   * Then  ログ出力時にヘッダーとボディ項目がマスクされる
   * </pre>
   */
  @Test
  @DisplayName("マスク対象ヘッダーとボディ項目がログでマスクされること")
  void shouldMaskHeaderAndBodyInLogs(final CapturedOutput output) throws Exception {
    mockMvc
        .perform(
            post("/api/v1/orders")
                .with(httpBasic(basicUsername, basicPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Request-Id", "request-123")
                .header("X-Forwarded-For", "203.0.113.10, 203.0.113.11")
                .content(
                    """
                    {
                      "orderedAt": "invalid-date-time",
                      "password": "secret-password"
                    }
                    """))
        .andExpect(status().isBadRequest());

    assertThat(output)
        .contains("[request] http method: POST")
        .contains("[request] request uri: /api/v1/orders")
        .contains("[request] content-type: application/json")
        .contains("[request] client ip: 203.0.113.10")
        .contains("[request] request id: request-123")
        .contains("[request] query string:")
        .contains("[request] parameters:")
        .contains("Authorization=****")
        .contains("X-Request-Id=request-123")
        .contains("X-Forwarded-For=203.0.113.10, 203.0.113.11")
        .contains("[request] body:")
        .contains("\"orderedAt\":\"invalid-date-time\"")
        .contains("\"password\":\"****\"")
        .contains("[response] status: 400")
        .contains("[response] content-type: application/json")
        .contains("[response] duration(ms):")
        .contains("[response] headers:")
        .contains("[response] body:")
        .contains("\"status\":400")
        .contains("\"error\":\"BAD_REQUEST\"")
        .contains("\"message\":\"リクエストボディの形式が不正です。\"")
        .contains("\"path\":\"/api/v1/orders\"")
        .contains("\"validationErrors\":[]")
        .contains("[response] size(bytes):");
  }

  /**
   *
   *
   * <pre>
   * Given 不正な文字エンコーディングを持つJSONリクエストを送信する
   * When  リクエストレスポンスログフィルターを実行する
   * Then  UTF-8にフォールバックしてレスポンスが返却される
   * </pre>
   */
  @Test
  @DisplayName("不正な文字エンコーディングでもUTF-8にフォールバックできること")
  void shouldFallbackToUtf8WhenRequestEncodingIsInvalid() throws Exception {
    RequestResponseLogFilter filter =
        new RequestResponseLogFilter(new LogMasker(new ObjectMapper()));
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setMethod("POST");
    request.setRequestURI("/test");
    request.setContentType(MediaType.APPLICATION_JSON_VALUE);
    request.setCharacterEncoding("invalid-charset");
    request.setContent(
        """
        {"password":"secret-password"}
        """
            .getBytes());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    FilterChain filterChain =
        (req, res) -> {
          HttpServletResponse actualResponse = (HttpServletResponse) res;
          actualResponse.setStatus(200);
          actualResponse.getWriter().write("{\"status\":\"ok\"}");
        };

    assertDoesNotThrow(() -> filter.doFilter(request, response, filterChain));
    assertEquals(200, response.getStatus());
    assertThat(response.getContentAsString()).contains("\"status\":\"ok\"");
  }
}

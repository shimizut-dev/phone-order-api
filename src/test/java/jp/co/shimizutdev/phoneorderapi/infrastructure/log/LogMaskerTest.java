package jp.co.shimizutdev.phoneorderapi.infrastructure.log;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** デフォルトログマスキングのテスト */
class LogMaskerTest {

  private final LogMasker logMasker = new LogMasker(new ObjectMapper());

  /**
   *
   *
   * <pre>
   * Given マスク対象ヘッダーを用意する
   * When ヘッダー値をマスキングする
   * Then ヘッダー値がマスク文字列に置き換えられる
   * </pre>
   */
  @Test
  @DisplayName("マスク対象ヘッダーはマスクされること")
  void shouldMaskTargetHeader() {
    String actual = logMasker.maskHeader("Authorization", "Bearer secret-token");

    assertThat(actual).isEqualTo("****");
  }

  /**
   *
   *
   * <pre>
   * Given マスク対象外のヘッダーを用意する
   * When ヘッダー値をマスキングする
   * Then 元のヘッダー値がそのまま返る
   * </pre>
   */
  @Test
  @DisplayName("マスク対象外のヘッダーはそのまま返すこと")
  void shouldReturnOriginalValueWhenHeaderIsNotMaskTarget() {
    String actual = logMasker.maskHeader("Content-Type", "application/json");

    assertThat(actual).isEqualTo("application/json");
  }

  /**
   *
   *
   * <pre>
   * Given マスク対象項目を含む JSON 文字列を用意する
   * When テキストをマスキングする
   * Then ネストした項目や配列要素も含めてマスクされる
   * </pre>
   */
  @Test
  @DisplayName("JSON 文字列のマスク対象項目を再帰的にマスクできること")
  void shouldMaskNestedJsonText() {
    String text =
        """
        {
          "token": "secret-token",
          "profile": {
            "email": "taro@example.com",
            "name": "taro"
          },
          "children": [
            {
              "password": "secret-password"
            }
          ]
        }
        """;

    String actual = logMasker.maskText(text);

    assertThat(actual)
        .isEqualTo(
            "{\"token\":\"****\",\"profile\":{\"email\":\"****\",\"name\":\"taro\"},"
                + "\"children\":[{\"password\":\"****\"}]}");
  }

  /**
   *
   *
   * <pre>
   * Given camelCase の機微情報項目を含む JSON 文字列
   * When テキストをマスキングする
   * Then camelCase の機微情報項目もマスクされること
   * </pre>
   */
  @Test
  @DisplayName("camelCase の機微情報項目を JSON 文字列でもマスクできること")
  void shouldMaskCamelCaseJsonFields() {
    String text =
        """
        {
          "accessToken": "secret-token",
          "profile": {
            "phoneNumber": "09012345678",
            "apiKey": "secret-api-key"
          }
        }
        """;

    String actual = logMasker.maskText(text);

    assertThat(actual)
        .isEqualTo(
            "{\"accessToken\":\"****\",\"profile\":{\"phoneNumber\":\"****\","
                + "\"apiKey\":\"****\"}}");
  }

  /**
   *
   *
   * <pre>
   * Given 改行を含む非 JSON 文字列を用意する
   * When テキストをマスキングする
   * Then 改行が正規化された 1 行文字列になる
   * </pre>
   */
  @Test
  @DisplayName("JSON でない文字列は 1 行に正規化されること")
  void shouldNormalizeWhitespaceWhenTextIsNotJson() {
    String text = "line1\n  line2\r\nline3";

    String actual = logMasker.maskText(text);

    assertThat(actual).isEqualTo("line1 line2 line3");
  }

  /**
   *
   *
   * <pre>
   * Given マスク対象項目を含むオブジェクトを用意する
   * When オブジェクトをマスキングする
   * Then マスク対象項目だけがマスク文字列に置き換えられる
   * </pre>
   */
  @Test
  @DisplayName("オブジェクトのマスク対象項目をマスクできること")
  void shouldMaskObjectFields() {
    Map<String, String> request = new LinkedHashMap<>();
    request.put("orderedAt", "2026-04-07T10:15:30+09:00");
    request.put("password", "secret-password");
    request.put("email", "taro@example.com");

    String actual = logMasker.maskObject(request);

    assertThat(actual)
        .isEqualTo(
            "{\"orderedAt\":\"2026-04-07T10:15:30+09:00\","
                + "\"password\":\"****\",\"email\":\"****\"}");
  }

  /**
   *
   *
   * <pre>
   * Given camelCase の機微情報項目を含むオブジェクト
   * When オブジェクトをマスキングする
   * Then camelCase の機微情報項目もマスクされること
   * </pre>
   */
  @Test
  @DisplayName("camelCase の機微情報項目をオブジェクトでもマスクできること")
  void shouldMaskCamelCaseObjectFields() {
    Map<String, String> request = new LinkedHashMap<>();
    request.put("orderedAt", "2026-04-07T10:15:30+09:00");
    request.put("accessToken", "secret-token");
    request.put("phoneNumber", "09012345678");
    request.put("apiKey", "secret-api-key");

    String actual = logMasker.maskObject(request);

    assertThat(actual)
        .isEqualTo(
            "{\"orderedAt\":\"2026-04-07T10:15:30+09:00\","
                + "\"accessToken\":\"****\",\"phoneNumber\":\"****\",\"apiKey\":\"****\"}");
  }
}

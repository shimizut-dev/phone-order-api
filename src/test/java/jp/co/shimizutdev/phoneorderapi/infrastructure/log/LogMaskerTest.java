package jp.co.shimizutdev.phoneorderapi.infrastructure.log;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** デフォルトログマスキング実装テスト */
class LogMaskerTest {

  private final LogMasker logMasker = new LogMasker(new ObjectMapper());

  /**
   *
   *
   * <pre>
   * Given マスク対象ヘッダーを用意する
   * When ヘッダー値をマスキングする
   * Then ヘッダー値がマスク文字列へ変換される
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
   * Given マスク対象外ヘッダーを用意する
   * When ヘッダー値をマスキングする
   * Then 元のヘッダー値がそのまま返る
   * </pre>
   */
  @Test
  @DisplayName("マスク対象外ヘッダーはそのまま返すこと")
  void shouldReturnOriginalValueWhenHeaderIsNotMaskTarget() {
    String actual = logMasker.maskHeader("Content-Type", "application/json");

    assertThat(actual).isEqualTo("application/json");
  }

  /**
   *
   *
   * <pre>
   * Given マスク対象項目を含むJSON文字列を用意する
   * When テキストをマスキングする
   * Then ネストした項目や配列要素も含めてマスクされる
   * </pre>
   */
  @Test
  @DisplayName("JSON文字列のマスク対象項目を再帰的にマスクできること")
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
            "{\"token\":\"****\",\"profile\":{\"email\":\"****\",\"name\":\"taro\"},\"children\":[{\"password\":\"****\"}]}");
  }

  /**
   *
   *
   * <pre>
   * Given 改行を含む非JSON文字列を用意する
   * When テキストをマスキングする
   * Then 改行と連続空白が正規化された1行文字列になる
   * </pre>
   */
  @Test
  @DisplayName("JSONでない文字列は1行化されること")
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
   * Then マスク対象項目だけがマスク文字列へ変換される
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
            "{\"orderedAt\":\"2026-04-07T10:15:30+09:00\",\"password\":\"****\",\"email\":\"****\"}");
  }
}

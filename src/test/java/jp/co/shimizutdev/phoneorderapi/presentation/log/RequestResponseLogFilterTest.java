package jp.co.shimizutdev.phoneorderapi.presentation.log;

import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
import jp.co.shimizutdev.phoneorderapi.support.ResetLogLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * リクエスト/レスポンスのログを出力するフィルタテスト
 */
@SpringBootTest(properties = "logging.level.jp.co.shimizutdev.phoneorderapi.presentation.log.RequestResponseLogFilter=DEBUG")
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
@ResetLogLevel(RequestResponseLogFilter.class)
class RequestResponseLogFilterTest extends AbstractPostgreSQLTest {

    /**
     * MockMvc
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * <pre>
     * Given マスク対象ヘッダーとマスク対象項目を含むリクエストを用意する
     * When 注文登録APIを実行する
     * Then ログ出力時にヘッダーとボディ項目がマスクされる
     * </pre>
     */
    @Test
    @DisplayName("マスク対象ヘッダーとボディ項目がログでマスクされること")
    void shouldMaskHeaderAndBodyInLogs(final CapturedOutput output) throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer secret-token")
                .header("X-Request-Id", "request-123")
                .header("X-Forwarded-For", "203.0.113.10, 203.0.113.11")
                .content("""
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
}

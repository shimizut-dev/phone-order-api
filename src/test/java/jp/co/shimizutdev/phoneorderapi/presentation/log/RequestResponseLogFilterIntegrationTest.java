package jp.co.shimizutdev.phoneorderapi.presentation.log;

import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLIntegrationTest;
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
 * リクエスト/レスポンスのログを出力するフィルタ統合テスト
 */
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class RequestResponseLogFilterIntegrationTest extends AbstractPostgreSQLIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * <pre>
     * マスク対象ヘッダーとボディ項目がログでマスクされること。
     *
     * Given マスク対象ヘッダーとマスク対象項目を含むリクエストを用意する
     * When 注文登録APIを実行する
     * Then ログ出力時にヘッダーとボディ項目がマスクされる
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("マスク対象ヘッダーとボディ項目がログでマスクされること")
    void shouldMaskHeaderAndBodyInLogs(final CapturedOutput output) throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer secret-token")
                .content("""
                    {
                      "orderedAt": "invalid-date-time",
                      "password": "secret-password"
                    }
                    """))
            .andExpect(status().isBadRequest());

        assertThat(output).contains("[request] hTTP method: POST");
        assertThat(output).contains("[request] request uri: /api/v1/orders");
        assertThat(output).contains("Authorization=****");
        assertThat(output).contains("[request] body:");
        assertThat(output).contains("\"password\":\"****\"");
        assertThat(output).contains("[response] status: 400");
        assertThat(output).contains("[response] body:");
    }
}

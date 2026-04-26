package jp.co.shimizutdev.phoneorderapi.presentation.log;

import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
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
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class RequestResponseLogFilterTest extends AbstractPostgreSQLTest {

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

        assertThat(output)
            .contains("[request] http method: POST")
            .contains("[request] request uri: /api/v1/orders")
            .contains("Authorization=****")
            .contains("[request] body:")
            .contains("\"password\":\"****\"")
            .contains("[response] status: 400")
            .contains("[response] body:");
    }
}

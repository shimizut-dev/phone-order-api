package jp.co.shimizutdev.phoneorderapi.presentation.order;

import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 注文コントローラ統合テスト
 */
@SpringBootTest
@AutoConfigureMockMvc
@SqlMergeMode(MERGE)
@Sql(
    scripts = "classpath:sql/cleanup/cleanup-transaction-tables.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = "classpath:sql/cleanup/cleanup-transaction-tables.sql",
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD
)
class OrderControllerIntegrationTest extends AbstractPostgreSQLIntegrationTest {

    /**
     * MockMvc
     */
    @Autowired
    private MockMvc mockMvc;


    @Nested
    @DisplayName("GET /api/orders")
    class ListOrders {
        /**
         * <pre>
         * 注文一覧を取得できること。
         *
         * Given 注文データが複数件登録されている
         * When 注文一覧取得APIを実行する
         * Then 200 OK と注文一覧が返る
         * </pre>
         *
         * @throws Exception 例外
         */
        @Test
        @DisplayName("注文一覧を取得できること")
        @Sql(statements = {
            "insert into orders (id, order_code, ordered_at, order_status, created_by, updated_by) values (gen_random_uuid(), 'ORD000001', now(), '001', 'system', 'system')",
            "insert into orders (id, order_code, ordered_at, order_status, created_by, updated_by) values (gen_random_uuid(), 'ORD000002', now(), '002', 'system', 'system')"
        })
        void shouldReturnOrdersWhenListOrders() throws Exception {
            mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].orderCode", hasItem("ORD000001")))
                .andExpect(jsonPath("$[*].orderCode", hasItem("ORD000002")));
        }
    }

    @Nested
    @DisplayName("GET /api/orders/order-code/{orderCode}")
    class GetOrderByOrderCode {
        /**
         * <pre>
         * 注文コードで注文を取得できること。
         *
         * Given 注文データが登録されている
         * When 注文コードで注文取得APIを実行する
         * Then 200 OK と注文情報が返る
         * </pre>
         *
         * @throws Exception 例外
         */
        @Test
        @DisplayName("注文コードで注文を取得できること")
        @Sql(statements = {
            "insert into orders (id, order_code, ordered_at, order_status, created_by, updated_by) values (gen_random_uuid(), 'ORD000001', now(), '001', 'system', 'system')"
        })
        void shouldReturnOrderWhenOrderCodeExists() throws Exception {
            mockMvc.perform(get("/api/orders/order-code/{orderCode}", "ORD000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value("ORD000001"))
                .andExpect(jsonPath("$.orderStatus").value("001"));
        }

        /**
         * <pre>
         * 注文コードに対応する注文が存在しない場合は404を返すこと。
         *
         * Given 対象注文が登録されていない
         * When 注文コードで注文取得APIを実行する
         * Then 404 Not Found が返る
         * </pre>
         *
         * @throws Exception 例外
         */
        @Test
        @DisplayName("注文コードに対応する注文が存在しない場合は404を返すこと")
        void shouldReturnNotFoundWhenOrderCodeDoesNotExist() throws Exception {
            mockMvc.perform(get("/api/orders/order-code/{orderCode}", "ORD999999"))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/orders")
    class CreateOrder {
        /**
         * <pre>
         * 注文を登録できること。
         *
         * Given 正常な注文リクエストを用意する
         * When 注文登録APIを実行する
         * Then 201 Created と登録された注文情報が返る
         * </pre>
         *
         * @throws Exception 例外
         */
        @Test
        @DisplayName("注文を登録できること")
        void shouldCreateOrderWhenRequestIsValid() throws Exception {
            String requestBody = """
                {
                  "orderedAt": "2026-04-07T10:15:30+09:00"
                }
                """;

            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderCode", startsWith("ORD")))
                .andExpect(jsonPath("$.orderStatus").value("001"));
        }

        /**
         * <pre>
         * 必須項目が不足している場合は400を返すこと。
         *
         * Given 必須項目が不足したリクエストを用意する
         * When 注文登録APIを実行する
         * Then 400 Bad Request と入力エラー情報が返る
         * </pre>
         *
         * @throws Exception 例外
         */
        @Test
        @DisplayName("必須項目が不足している場合は400を返すこと")
        void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {
            String requestBody = """
                {
                }
                """;

            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("入力値が不正です。"))
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[*].field", hasItem("orderedAt")))
                .andExpect(jsonPath("$.validationErrors[*].message", hasItem("注文日時は必須です。")));
        }

        /**
         * <pre>
         * 不正形式のリクエストボディの場合は400を返すこと。
         *
         * Given 不正形式のリクエストボディを用意する
         * When 注文登録APIを実行する
         * Then 400 Bad Request と形式不正メッセージが返る
         * </pre>
         *
         * @throws Exception 例外
         */
        @Test
        @DisplayName("不正形式のリクエストボディの場合は400を返すこと")
        void shouldReturnBadRequestWhenRequestBodyFormatIsInvalid() throws Exception {
            String requestBody = """
                {
                  "orderedAt": "invalid-date-time"
                }
                """;

            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("リクエストボディの形式が不正です。"))
                .andExpect(jsonPath("$.validationErrors", hasSize(0)));
        }
    }
}

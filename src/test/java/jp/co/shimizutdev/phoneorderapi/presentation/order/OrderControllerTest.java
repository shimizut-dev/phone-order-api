package jp.co.shimizutdev.phoneorderapi.presentation.order;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jp.co.shimizutdev.phoneorderapi.application.order.OrderService;
import jp.co.shimizutdev.phoneorderapi.domain.common.PagingCondition;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCode;
import jp.co.shimizutdev.phoneorderapi.domain.order.Version;
import jp.co.shimizutdev.phoneorderapi.presentation.error.ApiErrorMessages;
import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

/** 注文コントローラテスト */
@SpringBootTest
@AutoConfigureMockMvc
@SqlMergeMode(MERGE)
@Sql(
    scripts = "classpath:sql/cleanup/cleanup-transaction-tables.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(
    scripts = "classpath:sql/cleanup/cleanup-transaction-tables.sql",
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderControllerTest extends AbstractPostgreSQLTest {

  /** MockMvc */
  @Autowired private MockMvc mockMvc;

  /** 注文サービス */
  @MockitoSpyBean private OrderService orderService;

  @Nested
  @DisplayName("GET /api/v1/orders")
  class GetOrders {

    /**
     *
     *
     * <pre>
     * Given 注文データが複数件登録されている
     * When 注文一覧取得APIを実行する
     * Then 200 OK とページング済み注文一覧レスポンスが返る
     * </pre>
     */
    @Test
    @DisplayName("注文一覧を取得できること")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:15:30+09:00', '001', 0, 'system', 'system')",
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000002', 'ORD000002',"
              + " '2026-04-07T11:00:00+09:00', '002', 1, 'system', 'system')"
        })
    void shouldReturnOrdersWhenGetOrders() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.items", hasSize(2)))
          .andExpect(jsonPath("$.items[0].orderCode").value("ORD000002"))
          .andExpect(jsonPath("$.items[0].orderedAt").value("2026-04-07T02:00:00Z"))
          .andExpect(jsonPath("$.items[0].orderStatus").value("002"))
          .andExpect(jsonPath("$.items[0].version").value(1))
          .andExpect(jsonPath("$.items[1].orderCode").value("ORD000001"))
          .andExpect(jsonPath("$.items[1].orderedAt").value("2026-04-07T01:15:30Z"))
          .andExpect(jsonPath("$.items[1].orderStatus").value("001"))
          .andExpect(jsonPath("$.items[1].version").value(0))
          .andExpect(jsonPath("$.page").value(0))
          .andExpect(jsonPath("$.size").value(20))
          .andExpect(jsonPath("$.totalElements").value(2))
          .andExpect(jsonPath("$.totalPages").value(1))
          .andExpect(jsonPath("$.hasNext").value(false))
          .andExpect(jsonPath("$.hasPrevious").value(false));
    }

    /**
     *
     *
     * <pre>
     * Given 注文データが3件登録されている
     * When page と size を指定して注文一覧取得APIを実行する
     * Then 指定ページの注文一覧とページ情報が返る
     * </pre>
     */
    @Test
    @DisplayName("注文一覧をページ指定で取得できること")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:00:00+09:00', '001', 0, 'system', 'system')",
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000002', 'ORD000002',"
              + " '2026-04-07T11:00:00+09:00', '001', 0, 'system', 'system')",
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000003', 'ORD000003',"
              + " '2026-04-07T12:00:00+09:00', '001', 0, 'system', 'system')"
        })
    void shouldReturnOrdersWhenPageAndSizeAreSpecified() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders").param("page", "1").param("size", "2"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.items", hasSize(1)))
          .andExpect(jsonPath("$.items[0].orderCode").value("ORD000001"))
          .andExpect(jsonPath("$.items[0].orderedAt").value("2026-04-07T01:00:00Z"))
          .andExpect(jsonPath("$.items[0].orderStatus").value("001"))
          .andExpect(jsonPath("$.items[0].version").value(0))
          .andExpect(jsonPath("$.page").value(1))
          .andExpect(jsonPath("$.size").value(2))
          .andExpect(jsonPath("$.totalElements").value(3))
          .andExpect(jsonPath("$.totalPages").value(2))
          .andExpect(jsonPath("$.hasNext").value(false))
          .andExpect(jsonPath("$.hasPrevious").value(true));
    }

    /**
     *
     *
     * <pre>
     * Given 注文データが2件登録されている
     * When 総ページ数を超える page を指定して注文一覧取得APIを実行する
     * Then 空の注文一覧と指定ページのページ情報が返る
     * </pre>
     */
    @Test
    @DisplayName("範囲外ページは空ページを返すこと")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:00:00+09:00', '001', 0, 'system', 'system')",
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000002', 'ORD000002',"
              + " '2026-04-07T11:00:00+09:00', '001', 0, 'system', 'system')"
        })
    void shouldReturnEmptyPageWhenPageIsOutOfRange() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders").param("page", "2").param("size", "2"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.items", hasSize(0)))
          .andExpect(jsonPath("$.page").value(2))
          .andExpect(jsonPath("$.size").value(2))
          .andExpect(jsonPath("$.totalElements").value(2))
          .andExpect(jsonPath("$.totalPages").value(1))
          .andExpect(jsonPath("$.hasNext").value(false))
          .andExpect(jsonPath("$.hasPrevious").value(true));
    }

    /**
     *
     *
     * <pre>
     * Given 不正な page を用意する
     * When 注文一覧取得APIを実行する
     * Then 400 Bad Request と入力エラー情報が返る
     * </pre>
     */
    @Test
    @DisplayName("pageが不正な場合は400を返すこと")
    void shouldReturnBadRequestWhenPageIsInvalid() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders").param("page", "-1"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.VALIDATION_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders"))
          .andExpect(jsonPath("$.validationErrors", hasSize(1)))
          .andExpect(jsonPath("$.validationErrors[0].field").value("page"))
          .andExpect(jsonPath("$.validationErrors[0].message").isString());
    }

    /**
     *
     *
     * <pre>
     * Given 不正な size を用意する
     * When 注文一覧取得APIを実行する
     * Then 400 Bad Request と入力エラー情報が返る
     * </pre>
     */
    @Test
    @DisplayName("sizeが不正な場合は400を返すこと")
    void shouldReturnBadRequestWhenSizeIsInvalid() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders").param("size", "101"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.VALIDATION_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders"))
          .andExpect(jsonPath("$.validationErrors", hasSize(1)))
          .andExpect(jsonPath("$.validationErrors[0].field").value("size"))
          .andExpect(jsonPath("$.validationErrors[0].message").isString());
    }

    /**
     *
     *
     * <pre>
     * Given 注文サービスでRuntimeExceptionが発生する
     * When 注文一覧取得APIを実行する
     * Then 500 Internal Server Errorが返る
     * </pre>
     */
    @Test
    @DisplayName("注文一覧取得時に想定外エラーが発生した場合は500を返すこと")
    void shouldReturnInternalServerErrorWhenUnexpectedErrorOccurs() throws Exception {
      doThrow(new RuntimeException("unexpected error occurred"))
          .when(orderService)
          .getOrders(any(PagingCondition.class));

      mockMvc
          .perform(get("/api/v1/orders"))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.status").value(500))
          .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.INTERNAL_SERVER_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }
  }

  @Nested
  @DisplayName("GET /api/v1/orders/{orderCode}")
  class GetOrderByOrderCode {

    /**
     *
     *
     * <pre>
     * Given 注文データが登録されている
     * When 注文コードで注文取得APIを実行する
     * Then 200 OK と注文情報が返る
     * </pre>
     */
    @Test
    @DisplayName("注文コードで注文を取得できること")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:15:30+09:00', '001', 2, 'system', 'system')"
        })
    void shouldReturnOrderWhenOrderCodeExists() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders/{orderCode}", "ORD000001"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.orderCode").value("ORD000001"))
          .andExpect(jsonPath("$.orderedAt").value("2026-04-07T01:15:30Z"))
          .andExpect(jsonPath("$.orderStatus").value("001"))
          .andExpect(jsonPath("$.version").value(2));
    }

    /**
     *
     *
     * <pre>
     * Given 対象注文が登録されていない
     * When 注文コードで注文取得APIを実行する
     * Then 404 Not Found が返る
     * </pre>
     */
    @Test
    @DisplayName("注文コードに対応する注文が存在しない場合は404を返すこと")
    void shouldReturnNotFoundWhenOrderCodeDoesNotExist() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders/{orderCode}", "ORD999999"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.status").value(404))
          .andExpect(jsonPath("$.error").value("NOT_FOUND"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.ORDER_NOT_FOUND))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/ORD999999"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }

    /**
     *
     *
     * <pre>
     * Given 不正形式の注文コードを用意する
     * When 注文コードで注文取得APIを実行する
     * Then 400 Bad Request が返る
     * </pre>
     */
    @Test
    @DisplayName("注文コード形式が不正な場合は400を返すこと")
    void shouldReturnBadRequestWhenOrderCodeFormatIsInvalid() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders/{orderCode}", "INVALID"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.VALIDATION_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/INVALID"))
          .andExpect(jsonPath("$.validationErrors", hasSize(2)))
          .andExpect(jsonPath("$.validationErrors[0].field").value("orderCode"))
          .andExpect(jsonPath("$.validationErrors[0].message").isString())
          .andExpect(jsonPath("$.validationErrors[1].field").value("orderCode"))
          .andExpect(jsonPath("$.validationErrors[1].message").isString());
    }

    /**
     *
     *
     * <pre>
     * Given 注文サービスでRuntimeExceptionが発生する
     * When 注文コードで注文取得APIを実行する
     * Then 500 Internal Server Error が返る
     * </pre>
     */
    @Test
    @DisplayName("注文取得時に想定外エラーが発生した場合は500を返すこと")
    void shouldReturnInternalServerErrorWhenGetOrderUnexpectedErrorOccurs() throws Exception {
      doThrow(new RuntimeException("unexpected error occurred"))
          .when(orderService)
          .getOrderByOrderCode(OrderCode.of("ORD000001"));

      mockMvc
          .perform(get("/api/v1/orders/{orderCode}", "ORD000001"))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.status").value(500))
          .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.INTERNAL_SERVER_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/ORD000001"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }

    /**
     *
     *
     * <pre>
     * Given DBに不正な注文ステータスの注文データが登録されている
     * When 注文コードで注文取得APIを実行する
     * Then 500 Internal Server Error が返る
     * </pre>
     */
    @Test
    @DisplayName("DB上の注文ステータスが不正な場合は500を返すこと")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:15:30+09:00', '999', 0, 'system', 'system')"
        })
    void shouldReturnInternalServerErrorWhenPersistedOrderStatusIsInvalid() throws Exception {
      mockMvc
          .perform(get("/api/v1/orders/{orderCode}", "ORD000001"))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.status").value(500))
          .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.INTERNAL_SERVER_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/ORD000001"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }
  }

  @Nested
  @DisplayName("POST /api/v1/orders")
  class CreateOrder {

    /**
     *
     *
     * <pre>
     * Given 正常な注文リクエストを用意する
     * When 注文登録APIを実行する
     * Then 201 Created と登録された注文情報が返る
     * </pre>
     */
    @Test
    @DisplayName("注文を登録できること")
    void shouldCreateOrderWhenRequestIsValid() throws Exception {
      String requestBody =
          """
          {
            "orderedAt": "2026-04-07T10:15:30+09:00"
          }
          """;

      mockMvc
          .perform(
              post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content(requestBody))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.orderCode").value("ORD000001"))
          .andExpect(jsonPath("$.orderedAt").value("2026-04-07T01:15:30Z"))
          .andExpect(jsonPath("$.orderStatus").value("001"))
          .andExpect(jsonPath("$.version").value(0));
    }

    /**
     *
     *
     * <pre>
     * Given 必須項目が不足したリクエストを用意する
     * When 注文登録APIを実行する
     * Then 400 Bad Request と入力エラー情報が返る
     * </pre>
     */
    @Test
    @DisplayName("必須項目が不足している場合は400を返すこと")
    void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {
      mockMvc
          .perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content("{ }"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("入力値が不正です。"))
          .andExpect(jsonPath("$.path").value("/api/v1/orders"))
          .andExpect(jsonPath("$.validationErrors", hasSize(1)))
          .andExpect(jsonPath("$.validationErrors[0].field").value("orderedAt"))
          .andExpect(jsonPath("$.validationErrors[0].message").value("必須項目です。"));
    }

    /**
     *
     *
     * <pre>
     * Given 不正形式のリクエストボディを用意する
     * When 注文登録APIを実行する
     * Then 400 Bad Request と形式不正メッセージが返る
     * </pre>
     */
    @Test
    @DisplayName("不正形式のリクエストボディの場合は400を返すこと")
    void shouldReturnBadRequestWhenRequestBodyFormatIsInvalid() throws Exception {
      String requestBody =
          """
          {
            "orderedAt": "invalid-date-time"
          }
          """;

      mockMvc
          .perform(
              post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value("リクエストボディの形式が不正です。"))
          .andExpect(jsonPath("$.path").value("/api/v1/orders"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }

    /**
     *
     *
     * <pre>
     * Given 50文字を超える X-User-Id を含む注文リクエストを用意する
     * When 注文登録APIを実行する
     * Then 400 Bad Request と監査ユーザー不正メッセージが返る
     * </pre>
     */
    @Test
    @DisplayName("X-User-Idが50文字を超える場合は400を返すこと")
    void shouldReturnBadRequestWhenUserIdHeaderIsTooLong() throws Exception {
      String requestBody =
          """
          {
            "orderedAt": "2026-04-07T10:15:30+09:00"
          }
          """;

      mockMvc
          .perform(
              post("/api/v1/orders")
                  .contentType(MediaType.APPLICATION_JSON)
                  .header("X-User-Id", "a".repeat(51))
                  .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.INVALID_AUDITOR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }

    /**
     *
     *
     * <pre>
     * Given 注文サービスでRuntimeExceptionが発生する
     * When 注文登録APIを実行する
     * Then 500 Internal Server Error が返る
     * </pre>
     */
    @Test
    @DisplayName("注文登録時に想定外エラーが発生した場合は500を返すこと")
    void shouldReturnInternalServerErrorWhenCreateOrderUnexpectedErrorOccurs() throws Exception {
      String requestBody =
          """
          {
            "orderedAt": "2026-04-07T10:15:30+09:00"
          }
          """;

      doThrow(new RuntimeException("unexpected error occurred"))
          .when(orderService)
          .createOrder(any());

      mockMvc
          .perform(
              post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON).content(requestBody))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.status").value(500))
          .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.INTERNAL_SERVER_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }
  }

  @Nested
  @DisplayName("POST /api/v1/orders/{orderCode}/cancel")
  class CancelOrder {

    /**
     *
     *
     * <pre>
     * Given 注文データが登録されている
     * When 注文キャンセルAPIを実行する
     * Then 200 OK とキャンセル済み注文情報が返る
     * </pre>
     */
    @Test
    @DisplayName("注文をキャンセルできること")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:15:30+09:00', '001', 0, 'system', 'system')"
        })
    void shouldCancelOrderWhenOrderExists() throws Exception {
      String requestBody =
          """
          {
            "version": 0
          }
          """;

      mockMvc
          .perform(
              post("/api/v1/orders/{orderCode}/cancel", "ORD000001")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.orderCode").value("ORD000001"))
          .andExpect(jsonPath("$.orderedAt").value("2026-04-07T01:15:30Z"))
          .andExpect(jsonPath("$.orderStatus").value("006"))
          .andExpect(jsonPath("$.version").value(1));
    }

    /**
     *
     *
     * <pre>
     * Given versionが不足したキャンセルリクエストを用意する
     * When 注文キャンセルAPIを実行する
     * Then 400 Bad Request と入力エラー情報が返る
     * </pre>
     */
    @Test
    @DisplayName("version未指定は400になる")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:15:30+09:00', '001', 0, 'system', 'system')"
        })
    void shouldReturnBadRequestWhenVersionIsMissing() throws Exception {
      mockMvc
          .perform(
              post("/api/v1/orders/{orderCode}/cancel", "ORD000001")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{ }"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.VALIDATION_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/ORD000001/cancel"))
          .andExpect(jsonPath("$.validationErrors", hasSize(1)))
          .andExpect(jsonPath("$.validationErrors[0].field").value("version"))
          .andExpect(jsonPath("$.validationErrors[0].message").value("必須項目です。"));
    }

    /**
     *
     *
     * <pre>
     * Given 対象注文が登録されていない
     * When 注文キャンセルAPIを実行する
     * Then 404 Not Found が返る
     * </pre>
     */
    @Test
    @DisplayName("注文コードに対応する注文が存在しない場合は404を返すこと")
    void shouldReturnNotFoundWhenCancelTargetDoesNotExist() throws Exception {
      String requestBody =
          """
          {
            "version": 0
          }
          """;

      mockMvc
          .perform(
              post("/api/v1/orders/{orderCode}/cancel", "ORD999999")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.status").value(404))
          .andExpect(jsonPath("$.error").value("NOT_FOUND"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.ORDER_NOT_FOUND))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/ORD999999/cancel"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }

    /**
     *
     *
     * <pre>
     * Given 不正形式の注文コードを用意する
     * When 注文キャンセルAPIを実行する
     * Then 400 Bad Request が返る
     * </pre>
     */
    @Test
    @DisplayName("キャンセル対象の注文コード形式が不正な場合は400を返すこと")
    void shouldReturnBadRequestWhenCancelTargetOrderCodeFormatIsInvalid() throws Exception {
      String requestBody =
          """
          {
            "version": 0
          }
          """;

      mockMvc
          .perform(
              post("/api/v1/orders/{orderCode}/cancel", "INVALID")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.VALIDATION_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/INVALID/cancel"))
          .andExpect(jsonPath("$.validationErrors", hasSize(2)))
          .andExpect(jsonPath("$.validationErrors[0].field").value("orderCode"))
          .andExpect(jsonPath("$.validationErrors[0].message").isString())
          .andExpect(jsonPath("$.validationErrors[1].field").value("orderCode"))
          .andExpect(jsonPath("$.validationErrors[1].message").isString());
    }

    /**
     *
     *
     * <pre>
     * Given 完了状態の注文データが登録されている
     * When 注文キャンセルAPIを実行する
     * Then 409 Conflict が返る
     * </pre>
     */
    @Test
    @DisplayName("キャンセル不可状態の注文は409を返すこと")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:15:30+09:00', '005', 0, 'system', 'system')"
        })
    void shouldReturnConflictWhenOrderCannotBeCancelled() throws Exception {
      String requestBody =
          """
          {
            "version": 0
          }
          """;

      mockMvc
          .perform(
              post("/api/v1/orders/{orderCode}/cancel", "ORD000001")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.status").value(409))
          .andExpect(jsonPath("$.error").value("CONFLICT"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.ORDER_CANNOT_BE_CANCELLED))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/ORD000001/cancel"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }

    /**
     *
     *
     * <pre>
     * Given 対象注文が登録されている
     * When 古いversionで注文キャンセルAPIを実行する
     * Then 409 Conflict が返る
     * </pre>
     */
    @Test
    @DisplayName("stale version は409になる")
    @Sql(
        statements = {
          "insert into orders (id, order_code, ordered_at, order_status, version, created_by,"
              + " updated_by) values ('00000000-0000-0000-0000-000000000001', 'ORD000001',"
              + " '2026-04-07T10:15:30+09:00', '001', 0, 'system', 'system')"
        })
    void shouldReturnConflictWhenVersionDoesNotMatch() throws Exception {
      String requestBody =
          """
          {
            "version": 1
          }
          """;

      mockMvc
          .perform(
              post("/api/v1/orders/{orderCode}/cancel", "ORD000001")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.status").value(409))
          .andExpect(jsonPath("$.error").value("CONFLICT"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.ORDER_VERSION_CONFLICT))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/ORD000001/cancel"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }

    /**
     *
     *
     * <pre>
     * Given 注文サービスでRuntimeExceptionが発生する
     * When 注文キャンセルAPIを実行する
     * Then 500 Internal Server Error が返る
     * </pre>
     */
    @Test
    @DisplayName("注文キャンセル時に想定外エラーが発生した場合は500を返すこと")
    void shouldReturnInternalServerErrorWhenCancelOrderUnexpectedErrorOccurs() throws Exception {
      String requestBody =
          """
          {
            "version": 0
          }
          """;

      doThrow(new RuntimeException("unexpected error occurred"))
          .when(orderService)
          .cancelOrder(OrderCode.of("ORD000001"), Version.of(0L));

      mockMvc
          .perform(
              post("/api/v1/orders/{orderCode}/cancel", "ORD000001")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.status").value(500))
          .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
          .andExpect(jsonPath("$.message").value(ApiErrorMessages.INTERNAL_SERVER_ERROR))
          .andExpect(jsonPath("$.path").value("/api/v1/orders/ORD000001/cancel"))
          .andExpect(jsonPath("$.validationErrors", hasSize(0)));
    }
  }
}

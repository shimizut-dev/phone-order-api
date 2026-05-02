package jp.co.shimizutdev.phoneorderapi.presentation.error;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import jp.co.shimizutdev.phoneorderapi.infrastructure.config.InvalidAuditorException;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.InvalidPersistedOrderException;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** API例外ハンドラテスト */
class ApiExceptionHandlerTest {

  /** MockMvc */
  private MockMvc mockMvc;

  /** テスト実行前処理 */
  @BeforeEach
  void setUp() {
    ObjectMapper objectMapper =
        new ObjectMapper().registerModule(new JavaTimeModule()).disable(WRITE_DATES_AS_TIMESTAMPS);

    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();

    mockMvc =
        MockMvcBuilders.standaloneSetup(new TestController())
            .setControllerAdvice(new ApiExceptionHandler())
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .setValidator(validator)
            .build();
  }

  /**
   *
   *
   * <pre>
   * Given API入力不正エラーを送出するAPIがある
   * When API入力不正エラー送出APIを実行する
   * Then 400 Bad Request と項目別エラーのレスポンスが返る
   * </pre>
   */
  @Test
  @DisplayName("API入力不正エラーは400に変換されること")
  void shouldReturnBadRequestWhenApiBadRequestExceptionOccurs() throws Exception {
    mockMvc
        .perform(get("/test/errors/api-bad-request"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value(ApiErrorMessages.VALIDATION_ERROR))
        .andExpect(jsonPath("$.path").value("/test/errors/api-bad-request"))
        .andExpect(jsonPath("$.validationErrors", hasSize(1)))
        .andExpect(jsonPath("$.validationErrors[0].field").value("orderCode"))
        .andExpect(jsonPath("$.validationErrors[0].message").value("illegal argument occurred"));
  }

  /**
   *
   *
   * <pre>
   * Given IllegalArgumentException を送出するAPIがある
   * When IllegalArgumentException送出APIを実行する
   * Then 500 Internal Server Error のレスポンスが返る
   * </pre>
   */
  @Test
  @DisplayName("IllegalArgumentExceptionは500に変換されること")
  void shouldReturnInternalServerErrorWhenIllegalArgumentExceptionOccurs() throws Exception {
    mockMvc
        .perform(get("/test/errors/illegal-argument"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
        .andExpect(jsonPath("$.message").value(ApiErrorMessages.INTERNAL_SERVER_ERROR))
        .andExpect(jsonPath("$.path").value("/test/errors/illegal-argument"))
        .andExpect(jsonPath("$.validationErrors", hasSize(0)));
  }

  /**
   *
   *
   * <pre>
   * Given バリデーションエラーとなるリクエストを受け付けるAPIがある
   * When 必須項目欠落のリクエストでAPIを実行する
   * Then 400 Bad Request とバリデーション詳細が返る
   * </pre>
   */
  @Test
  @DisplayName("バリデーションエラーは400に変換されること")
  void shouldReturnBadRequestWhenValidationErrorOccurs() throws Exception {
    mockMvc
        .perform(
            post("/test/errors/validation").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value(ApiErrorMessages.VALIDATION_ERROR))
        .andExpect(jsonPath("$.path").value("/test/errors/validation"))
        .andExpect(jsonPath("$.validationErrors", hasSize(1)))
        .andExpect(jsonPath("$.validationErrors[0].field").value("name"))
        .andExpect(jsonPath("$.validationErrors[0].message").value("name is required."));
  }

  /**
   *
   *
   * <pre>
   * Given 監査ユーザー不正例外を送出するAPIがある
   * When 監査ユーザー不正例外送出APIを実行する
   * Then 400 Bad Request のエラーレスポンスが返る
   * </pre>
   */
  @Test
  @DisplayName("監査ユーザー不正例外は400に変換されること")
  void shouldReturnBadRequestWhenInvalidAuditorExceptionOccurs() throws Exception {
    mockMvc
        .perform(get("/test/errors/invalid-auditor"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value(ApiErrorMessages.INVALID_AUDITOR))
        .andExpect(jsonPath("$.path").value("/test/errors/invalid-auditor"))
        .andExpect(jsonPath("$.validationErrors", hasSize(0)));
  }

  /**
   *
   *
   * <pre>
   * Given 想定外の例外を送出するAPIがある
   * When 想定外の例外送出APIを実行する
   * Then 500 Internal Server Error と汎用メッセージが返る
   * </pre>
   */
  @Test
  @DisplayName("想定外の例外は汎用メッセージの500に変換されること")
  void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
    mockMvc
        .perform(get("/test/errors/unexpected"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
        .andExpect(jsonPath("$.message").value(ApiErrorMessages.INTERNAL_SERVER_ERROR))
        .andExpect(jsonPath("$.path").value("/test/errors/unexpected"))
        .andExpect(jsonPath("$.validationErrors", hasSize(0)));
  }

  /**
   *
   *
   * <pre>
   * Given 永続化データ不整合例外を送出するAPIがある
   * When 永続化データ不整合例外送出APIを実行する
   * Then 500 Internal Server Error のエラーレスポンスが返る
   * </pre>
   */
  @Test
  @DisplayName("永続化データ不整合例外は500に変換されること")
  void shouldReturnInternalServerErrorWhenInvalidPersistedOrderExceptionOccurs() throws Exception {
    mockMvc
        .perform(get("/test/errors/invalid-persisted-order"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").isString())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
        .andExpect(jsonPath("$.message").value(ApiErrorMessages.INTERNAL_SERVER_ERROR))
        .andExpect(jsonPath("$.path").value("/test/errors/invalid-persisted-order"))
        .andExpect(jsonPath("$.validationErrors", hasSize(0)));
  }

  @RestController
  @RequestMapping("/test/errors")
  static class TestController {

    @GetMapping("/api-bad-request")
    String throwApiBadRequestException() {
      throw new ApiBadRequestException("orderCode", "illegal argument occurred");
    }

    @GetMapping("/illegal-argument")
    String throwIllegalArgumentException() {
      throw new IllegalArgumentException("illegal argument occurred");
    }

    @PostMapping("/validation")
    String validate(@Valid @RequestBody final TestRequest request) {
      return request.name();
    }

    @GetMapping("/invalid-auditor")
    String throwInvalidAuditorException() {
      throw new InvalidAuditorException(ApiErrorMessages.INVALID_AUDITOR);
    }

    @GetMapping("/unexpected")
    String throwUnexpectedException() {
      throw new IllegalStateException("boom");
    }

    @GetMapping("/invalid-persisted-order")
    String throwInvalidPersistedOrderException() {
      OrderJpaEntity orderJpaEntity = mock(OrderJpaEntity.class);
      when(orderJpaEntity.getId())
          .thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000001"));
      when(orderJpaEntity.getOrderCode()).thenReturn("ORD000001");
      throw InvalidPersistedOrderException.byOrderJpaEntity(
          "persisted order is invalid", orderJpaEntity);
    }
  }

  private record TestRequest(@NotBlank(message = "name is required.") String name) {}
}

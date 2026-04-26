package jp.co.shimizutdev.phoneorderapi.presentation.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.InvalidPersistedOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
            .standaloneSetup(new TestController())
            .setControllerAdvice(new ApiExceptionHandler())
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .setValidator(validator)
            .build();
    }

    /**
     * <pre>
     * IllegalArgumentException発生時に400エラーレスポンスを返すこと。
     *
     * Given IllegalArgumentExceptionを送出するAPIがある
     * When IllegalArgumentException発生APIを実行する
     * Then 400 Bad Requestのエラーレスポンスが返る
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("IllegalArgumentException is mapped to 400")
    void shouldReturnBadRequestWhenIllegalArgumentExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/errors/illegal-argument"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("illegal argument occurred"))
            .andExpect(jsonPath("$.path").value("/test/errors/illegal-argument"))
            .andExpect(jsonPath("$.validationErrors").isArray());
    }

    /**
     * <pre>
     * メッセージなしIllegalArgumentException発生時にバリデーションエラーメッセージで400エラーレスポンスを返すこと。
     *
     * Given メッセージなしIllegalArgumentExceptionを送出するAPIがある
     * When メッセージなしIllegalArgumentException発生APIを実行する
     * Then 400 Bad Requestとバリデーションエラーメッセージのエラーレスポンスが返る
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("IllegalArgumentException without a message falls back to the validation error message")
    void shouldReturnValidationErrorMessageWhenIllegalArgumentExceptionHasNoMessage() throws Exception {
        mockMvc.perform(get("/test/errors/illegal-argument-without-message"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(ApiErrorResponseMessages.VALIDATION_ERROR))
            .andExpect(jsonPath("$.path").value("/test/errors/illegal-argument-without-message"))
            .andExpect(jsonPath("$.validationErrors").isArray());
    }

    /**
     * <pre>
     * バリデーションエラー発生時に400エラーレスポンスを返すこと。
     *
     * Given バリデーションエラーとなるリクエストを受け付けるAPIがある
     * When 必須項目不足のリクエストでAPIを実行する
     * Then 400 Bad Requestとバリデーションエラー詳細が返る
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("Validation errors are mapped to 400")
    void shouldReturnBadRequestWhenValidationErrorOccurs() throws Exception {
        mockMvc.perform(post("/test/errors/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(ApiErrorResponseMessages.VALIDATION_ERROR))
            .andExpect(jsonPath("$.path").value("/test/errors/validation"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("name"))
            .andExpect(jsonPath("$.validationErrors[0].message").value("name is required."));
    }

    /**
     * <pre>
     * 非標準のHTTPステータスコードを指定したResponseStatusException発生時にそのステータスを保持して返すこと。
     *
     * Given 非標準ステータスコードのResponseStatusExceptionを送出するAPIがある
     * When カスタムステータス例外発生APIを実行する
     * Then 指定した非標準ステータスコードのエラーレスポンスが返る
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("Non-standard response statuses are preserved")
    void shouldReturnCustomStatusWhenResponseStatusExceptionUsesNonStandardCode() throws Exception {
        mockMvc.perform(get("/test/errors/custom-status"))
            .andExpect(status().is(499))
            .andExpect(jsonPath("$.status").value(499))
            .andExpect(jsonPath("$.error").value("HTTP_499"))
            .andExpect(jsonPath("$.message").value("client closed request"))
            .andExpect(jsonPath("$.path").value("/test/errors/custom-status"));
    }

    /**
     * <pre>
     * 想定外例外発生時に汎用的な500エラーレスポンスを返すこと。
     *
     * Given 想定外例外を送出するAPIがある
     * When 想定外例外発生APIを実行する
     * Then 500 Internal Server Errorと汎用エラーメッセージが返る
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("Unexpected exceptions are hidden behind a generic 500 response")
    void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/errors/unexpected"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value(ApiErrorResponseMessages.INTERNAL_SERVER_ERROR))
            .andExpect(jsonPath("$.path").value("/test/errors/unexpected"));
    }

    /**
     * <pre>
     * InvalidPersistedOrderException発生時に500エラーレスポンスを返すこと。
     *
     * Given InvalidPersistedOrderExceptionを送出するAPIがある
     * When InvalidPersistedOrderException発生APIを実行する
     * Then 500 Internal Server Errorのエラーレスポンスが返る
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("InvalidPersistedOrderException is mapped to 500")
    void shouldReturnInternalServerErrorWhenInvalidPersistedOrderExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/errors/invalid-persisted-order"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.message").value(ApiErrorResponseMessages.INTERNAL_SERVER_ERROR))
            .andExpect(jsonPath("$.path").value("/test/errors/invalid-persisted-order"))
            .andExpect(jsonPath("$.validationErrors").isArray());
    }

    @RestController
    @RequestMapping("/test/errors")
    static class TestController {

        @GetMapping("/illegal-argument")
        String throwIllegalArgumentException() {
            throw new IllegalArgumentException("illegal argument occurred");
        }

        @GetMapping("/illegal-argument-without-message")
        String throwIllegalArgumentExceptionWithoutMessage() {
            throw new IllegalArgumentException();
        }

        @PostMapping("/validation")
        String validate(@Valid @RequestBody final TestRequest request) {
            return request.name();
        }

        @GetMapping("/custom-status")
        String throwCustomStatusException() {
            throw new ResponseStatusException(HttpStatusCode.valueOf(499), "client closed request");
        }

        @GetMapping("/unexpected")
        String throwUnexpectedException() {
            throw new IllegalStateException("boom");
        }

        @GetMapping("/invalid-persisted-order")
        String throwInvalidPersistedOrderException() {
            throw new InvalidPersistedOrderException("persisted order is invalid");
        }
    }

    private record TestRequest(
        @NotBlank(message = "name is required.")
        String name
    ) {
    }
}

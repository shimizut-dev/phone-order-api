package jp.co.shimizutdev.phoneorderapi.presentation.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * グローバル例外ハンドラ統合テスト
 */
class GlobalExceptionHandlerIntegrationTest {

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
            .setControllerAdvice(new GlobalExceptionHandler())
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .setValidator(validator)
            .build();
    }

    /**
     * <pre>
     * IllegalArgumentException は400で返ること。
     *
     * Given IllegalArgumentException を送出するAPIを用意する
     * When 対象APIを実行する
     * Then 400 Bad Request とエラーレスポンスが返る
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("IllegalArgumentException は400で返ること")
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
     * バリデーションエラーは400で返ること。
     *
     * Given 必須項目が不足したリクエストを用意する
     * When バリデーション対象APIを実行する
     * Then 400 Bad Request とバリデーションエラー情報が返る
     * </pre>
     *
     * @throws Exception 例外
     */
    @Test
    @DisplayName("バリデーションエラーは400で返ること")
    void shouldReturnBadRequestWhenValidationErrorOccurs() throws Exception {
        mockMvc.perform(post("/test/errors/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("入力値が不正です。"))
            .andExpect(jsonPath("$.path").value("/test/errors/validation"))
            .andExpect(jsonPath("$.validationErrors[0].field").value("name"))
            .andExpect(jsonPath("$.validationErrors[0].message").value("name is required."));
    }

    @RestController
    @RequestMapping("/test/errors")
    static class TestController {

        @GetMapping("/illegal-argument")
        String throwIllegalArgumentException() {
            throw new IllegalArgumentException("illegal argument occurred");
        }

        @PostMapping("/validation")
        String validate(@Valid @RequestBody TestRequest request) {
            return request.name();
        }
    }

    private record TestRequest(
        @NotBlank(message = "name is required.")
        String name
    ) {
    }
}

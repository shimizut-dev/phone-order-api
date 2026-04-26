package jp.co.shimizutdev.phoneorderapi.presentation.log;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * トレースIDをMDCへ設定するFilterテスト
 */
class TraceIdFilterTest {

    /**
     * テスト後処理
     */
    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    /**
     * <pre>
     * フィルタチェーン実行中にトレースIDが設定されること。
     *
     * Given トレースIDフィルタを用意する
     * When フィルタを実行する
     * Then フィルタチェーン実行中にMDCへUUID形式のトレースIDが設定される
     * </pre>
     */
    @Test
    @DisplayName("フィルタチェーン実行中にトレースIDが設定されること")
    void shouldSetTraceIdWhileFilterChainIsRunning() {
        TraceIdFilter traceIdFilter = new TraceIdFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> actualTraceId = new AtomicReference<>();
        FilterChain filterChain = (req, res) -> actualTraceId.set(MDC.get(TraceIdFilter.TRACE_ID_KEY));

        assertDoesNotThrow(() -> traceIdFilter.doFilter(request, response, filterChain));

        assertTrue(isUuid(actualTraceId.get()));
    }

    /**
     * <pre>
     * フィルタチェーン実行後にトレースIDが削除されること。
     *
     * Given トレースIDフィルタを用意する
     * When フィルタを実行する
     * Then フィルタチェーン実行後にMDCからトレースIDが削除される
     * </pre>
     */
    @Test
    @DisplayName("フィルタチェーン実行後にトレースIDが削除されること")
    void shouldRemoveTraceIdAfterFilterChainIsFinished() {
        TraceIdFilter traceIdFilter = new TraceIdFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = (req, res) -> {
        };

        assertDoesNotThrow(() -> traceIdFilter.doFilter(request, response, filterChain));

        assertNull(MDC.get(TraceIdFilter.TRACE_ID_KEY));
    }

    /**
     * UUID形式かどうかを判定する。
     *
     * @param value 値
     * @return UUID形式の場合true
     */
    private boolean isUuid(final String value) {
        if (value == null) {
            return false;
        }

        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}

package jp.co.shimizutdev.phoneorderapi.presentation.log;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.FilterChain;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/** トレースIDをMDCへ設定するFilterテスト */
class TraceIdFilterTest {

  /** テスト後処理 */
  @AfterEach
  void tearDown() {
    MDC.clear();
  }

  /**
   *
   *
   * <pre>
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
    assertEquals(actualTraceId.get(), response.getHeader("X-Trace-Id"));
  }

  /**
   *
   *
   * <pre>
   * Given X-Request-Id ヘッダーを含むリクエストを用意する
   * When フィルタを実行する
   * Then MDC とレスポンスヘッダーに同じ traceId が設定される
   * </pre>
   */
  @Test
  @DisplayName("X-Request-Id を traceId として引き継ぐこと")
  void shouldReuseRequestIdHeaderAsTraceId() {
    TraceIdFilter traceIdFilter = new TraceIdFilter();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.addHeader("X-Request-Id", "request-123");
    AtomicReference<String> actualTraceId = new AtomicReference<>();
    FilterChain filterChain = (req, res) -> actualTraceId.set(MDC.get(TraceIdFilter.TRACE_ID_KEY));

    assertDoesNotThrow(() -> traceIdFilter.doFilter(request, response, filterChain));

    assertEquals("request-123", actualTraceId.get());
    assertEquals("request-123", response.getHeader("X-Trace-Id"));
    assertNull(MDC.get(TraceIdFilter.TRACE_ID_KEY));
  }

  /**
   *
   *
   * <pre>
   * Given 不正な X-Request-Id と有効な X-Correlation-Id を含むリクエストを用意する
   * When フィルタを実行する
   * Then X-Correlation-Id が traceId として設定される
   * </pre>
   */
  @Test
  @DisplayName("不正な X-Request-Id の場合は X-Correlation-Id を traceId として引き継ぐこと")
  void shouldFallbackToCorrelationIdWhenRequestIdIsInvalid() {
    TraceIdFilter traceIdFilter = new TraceIdFilter();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.addHeader("X-Request-Id", "invalid trace id");
    request.addHeader("X-Correlation-Id", "correlation-123");
    AtomicReference<String> actualTraceId = new AtomicReference<>();
    FilterChain filterChain = (req, res) -> actualTraceId.set(MDC.get(TraceIdFilter.TRACE_ID_KEY));

    assertDoesNotThrow(() -> traceIdFilter.doFilter(request, response, filterChain));

    assertEquals("correlation-123", actualTraceId.get());
    assertEquals("correlation-123", response.getHeader("X-Trace-Id"));
  }

  /**
   *
   *
   * <pre>
   * Given 不正な X-Request-Id を含むリクエストを用意する
   * When フィルタを実行する
   * Then UUID形式の traceId が新規生成される
   * </pre>
   */
  @Test
  @DisplayName("不正なヘッダーの場合はUUID形式の traceId を生成すること")
  void shouldGenerateTraceIdWhenHeadersAreInvalid() {
    TraceIdFilter traceIdFilter = new TraceIdFilter();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.addHeader("X-Request-Id", "a".repeat(129));
    AtomicReference<String> actualTraceId = new AtomicReference<>();
    FilterChain filterChain = (req, res) -> actualTraceId.set(MDC.get(TraceIdFilter.TRACE_ID_KEY));

    assertDoesNotThrow(() -> traceIdFilter.doFilter(request, response, filterChain));

    assertTrue(isUuid(actualTraceId.get()));
    assertEquals(actualTraceId.get(), response.getHeader("X-Trace-Id"));
  }

  /**
   *
   *
   * <pre>
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
    FilterChain filterChain = (req, res) -> {};

    assertDoesNotThrow(() -> traceIdFilter.doFilter(request, response, filterChain));

    assertNull(MDC.get(TraceIdFilter.TRACE_ID_KEY));
  }

  /**
   * UUID形式かどうかを判定する
   *
   * @param value UUID形式判定対象文字列
   * @return UUID形式の場合 true
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

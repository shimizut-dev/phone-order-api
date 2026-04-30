package jp.co.shimizutdev.phoneorderapi.presentation.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * トレースIDをMDCへ設定するFilter
 */
@Component
@Order(1)
@Slf4j
public class TraceIdFilter extends OncePerRequestFilter {

    /**
     * トレースIDキー
     */
    public static final String TRACE_ID_KEY = "traceId";

    /**
     * リクエストから traceId を引き継ぐためのヘッダー名
     */
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    /**
     * リクエストから correlationId を引き継ぐためのヘッダー名
     */
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    /**
     * レスポンスへ traceId を返却するためのヘッダー名
     */
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    /**
     * 受け入れる traceId の最大文字数
     */
    private static final int TRACE_ID_MAX_LENGTH = 128;

    /**
     * 受け入れる traceId の文字種パターン
     */
    private static final Pattern TRACE_ID_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+$");

    /**
     * トレースIDを設定してフィルタチェーンを実行する
     *
     * @param request     HTTPリクエスト
     * @param response    HTTPレスポンス
     * @param filterChain フィルタチェーン
     * @throws ServletException ServletException
     * @throws IOException      IOException
     */
    @Override
    protected void doFilterInternal(
        @NonNull final HttpServletRequest request,
        @NonNull final HttpServletResponse response,
        @NonNull final FilterChain filterChain) throws ServletException, IOException {

        String traceId = resolveTraceId(request);

        try {
            MDC.put(TRACE_ID_KEY, traceId);
            response.setHeader(TRACE_ID_HEADER, traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }

    /**
     * リクエストから traceId を解決する
     *
     * @param request HTTP リクエスト
     * @return traceId
     */
    private String resolveTraceId(final HttpServletRequest request) {
        String requestId = request.getHeader(REQUEST_ID_HEADER);

        if (isValidTraceId(requestId)) {
            return requestId.trim();
        }

        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (isValidTraceId(correlationId)) {
            return correlationId.trim();
        }

        return UUID.randomUUID().toString();
    }

    /**
     * 指定された traceId が受け入れ可能かを検証する
     *
     * @param traceId 検証対象の traceId
     * @return traceId が空でなく、長さ制限内で、許可パターンに一致する場合は {@code true}
     */
    private boolean isValidTraceId(final String traceId) {
        if (traceId == null) {
            return false;
        }

        String normalizedTraceId = traceId.trim();

        return !normalizedTraceId.isEmpty()
            && normalizedTraceId.length() <= TRACE_ID_MAX_LENGTH
            && TRACE_ID_PATTERN.matcher(normalizedTraceId).matches();
    }
}

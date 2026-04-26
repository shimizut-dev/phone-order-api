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
     * トレースIDを設定してフィルタチェーンを実行する。
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

        if (requestId != null && !requestId.isBlank()) {
            return requestId.trim();
        }

        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId != null && !correlationId.isBlank()) {
            return correlationId.trim();
        }

        return UUID.randomUUID().toString();
    }
}

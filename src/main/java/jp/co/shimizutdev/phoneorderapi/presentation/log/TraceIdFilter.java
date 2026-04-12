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
     * MDCキー
     */
    public static final String KEY = "traceId";

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

        try {
            MDC.put(KEY, UUID.randomUUID().toString());
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(KEY);
        }
    }
}

package jp.co.shimizutdev.phoneorderapi.presentation.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.shimizutdev.phoneorderapi.infrastructure.log.LogMasker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * リクエスト/レスポンスのログを出力するフィルタ
 */
@Component
@Order(2)
@Slf4j
@RequiredArgsConstructor
public class RequestResponseLogFilter extends OncePerRequestFilter {

    /**
     * 本文ログ最大文字数
     */
    private static final int MAX_LOG_BODY_LENGTH = 5000;

    /**
     * リクエスト本文キャッシュ上限バイト数
     */
    private static final int REQUEST_CONTENT_CACHE_LIMIT = 8192;

    /**
     * ログマスキング
     */
    private final LogMasker logMasker;

    /**
     * リクエスト / レスポンスのログを出力する。
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

        ContentCachingRequestWrapper wrappedRequest = wrapRequest(request);
        ContentCachingResponseWrapper wrappedResponse = wrapResponse(response);

        long startTimeMillis = System.currentTimeMillis();
        Exception exception = null;

        logRequest(wrappedRequest);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } catch (IOException | ServletException | RuntimeException ex) {
            exception = ex;
            throw ex;
        } finally {
            long durationMillis = System.currentTimeMillis() - startTimeMillis;
            logAfterCompletion(wrappedRequest, wrappedResponse, durationMillis, exception);
            wrappedResponse.copyBodyToResponse();
        }
    }

    /**
     * リクエストをラップする。
     *
     * @param request HTTPリクエスト
     * @return ラップ済みリクエスト
     */
    private ContentCachingRequestWrapper wrapRequest(final HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            return wrapper;
        }
        return new ContentCachingRequestWrapper(request, REQUEST_CONTENT_CACHE_LIMIT);
    }

    /**
     * レスポンスをラップする。
     *
     * @param response HTTPレスポンス
     * @return ラップ済みレスポンス
     */
    private ContentCachingResponseWrapper wrapResponse(final HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper wrapper) {
            return wrapper;
        }
        return new ContentCachingResponseWrapper(response);
    }

    /**
     * リクエストログを出力する。
     *
     * @param request HTTPリクエスト
     */
    private void logRequest(final ContentCachingRequestWrapper request) {
        log.info("[request] http method: {}", request.getMethod());
        log.info("[request] request uri: {}", request.getRequestURI());
        log.info("[request] content-type: {}", nullToEmpty(request.getContentType()));
        log.info("[request] client ip: {}", getClientIpAddress(request));
        log.info("[request] request id: {}", getRequestId(request));

        if (log.isDebugEnabled()) {
            log.debug("[request] query string: {}", nullToEmpty(request.getQueryString()));
            log.debug("[request] parameters: {}", getParameters(request));
            log.debug("[request] headers: {}", getHeaders(request));
        }
    }

    /**
     * パラメータを取得する。
     *
     * @param request HTTPリクエスト
     * @return パラメータ
     */
    private String getParameters(final ContentCachingRequestWrapper request) {
        return request.getParameterMap().entrySet().stream()
            .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
            .collect(Collectors.joining(","));
    }

    /**
     * リクエストヘッダーを取得する。
     *
     * @param request HTTPリクエスト
     * @return ヘッダー
     */
    private String getHeaders(final ContentCachingRequestWrapper request) {
        return Collections.list(request.getHeaderNames()).stream()
            .map(headerName -> headerName + "=" + logMasker.maskHeader(headerName, request.getHeader(headerName)))
            .collect(Collectors.joining(","));
    }

    /**
     * レスポンスヘッダーを取得する。
     *
     * @param response HTTPレスポンス
     * @return ヘッダー
     */
    private String getHeaders(final ContentCachingResponseWrapper response) {
        return response.getHeaderNames().stream()
            .map(headerName -> headerName + "=" + logMasker.maskHeader(headerName, response.getHeader(headerName)))
            .collect(Collectors.joining(","));
    }

    /**
     * リクエスト / レスポンスの完了後ログを出力する。
     *
     * @param request        HTTPリクエスト
     * @param response       HTTPレスポンス
     * @param durationMillis 処理時間
     * @param exception      例外
     */
    private void logAfterCompletion(
        final ContentCachingRequestWrapper request,
        final ContentCachingResponseWrapper response,
        final long durationMillis,
        final Exception exception) {

        log.info("[response] status: {}", response.getStatus());
        log.info("[response] content-type: {}", nullToEmpty(response.getContentType()));
        log.info("[response] duration(ms): {}", durationMillis);

        if (log.isDebugEnabled()) {
            log.debug("[request] body: {}", getRequestBody(request));
            log.debug("[response] headers: {}", getHeaders(response));
            log.debug("[response] body: {}", getResponseBody(response));
            log.debug("[response] size(bytes): {}", response.getContentAsByteArray().length);
        }

        if (exception != null) {
            log.warn("[exception] class: {}", exception.getClass().getName());
            log.warn("[exception] message: {}", nullToEmpty(exception.getMessage()));
        }
    }

    /**
     * リクエスト本文を取得する。
     *
     * @param request HTTPリクエスト
     * @return リクエスト本文
     */
    private String getRequestBody(final ContentCachingRequestWrapper request) {
        return getBody(
            request.getContentType(),
            request.getCharacterEncoding(),
            request.getContentAsByteArray()
        );
    }

    /**
     * レスポンス本文を取得する。
     *
     * @param response HTTPレスポンス
     * @return レスポンス本文
     */
    private String getResponseBody(final ContentCachingResponseWrapper response) {
        return getBody(
            response.getContentType(),
            response.getCharacterEncoding(),
            response.getContentAsByteArray()
        );
    }

    /**
     * 本文を取得する。
     *
     * @param contentType Content-Type
     * @param encoding    文字コード名
     * @param content     本文バイト配列
     * @return 本文
     */
    private String getBody(
        final String contentType,
        final String encoding,
        final byte[] content) {

        if (!isLoggableContentType(contentType)) {
            return "[unsupported content type]";
        }

        if (content.length == 0) {
            return "";
        }

        return abbreviate(logMasker.maskText(new String(content, getCharset(encoding))));
    }

    /**
     * Content-Type がログ出力対象か判定する。
     *
     * @param contentType Content-Type
     * @return 判定結果
     */
    private boolean isLoggableContentType(final String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return false;
        }

        return contentType.startsWith("application/json")
            || contentType.startsWith("application/xml")
            || contentType.startsWith("application/x-www-form-urlencoded")
            || contentType.startsWith("text/plain")
            || contentType.startsWith("text/html");
    }

    /**
     * リクエストIDを取得する。
     *
     * @param request HTTPリクエスト
     * @return リクエストID
     */
    private String getRequestId(final HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-Id");
        if (requestId == null || requestId.isBlank()) {
            requestId = request.getHeader("X-Correlation-Id");
        }
        return nullToEmpty(requestId);
    }

    /**
     * クライアントIPアドレスを取得する。
     *
     * @param request HTTPリクエスト
     * @return クライアントIPアドレス
     */
    private String getClientIpAddress(final HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * 文字コードを取得する。
     *
     * @param encoding 文字コード名
     * @return 文字コード
     */
    private Charset getCharset(final String encoding) {
        if (encoding == null || encoding.isBlank()) {
            return StandardCharsets.UTF_8;
        }
        return Charset.forName(encoding);
    }

    /**
     * 本文を最大文字数に省略する。
     *
     * @param body 本文
     * @return 省略後本文
     */
    private String abbreviate(final String body) {
        if (body == null) {
            return "";
        }

        if (body.length() <= MAX_LOG_BODY_LENGTH) {
            return body;
        }

        return body.substring(0, MAX_LOG_BODY_LENGTH) + "...(truncated)";
    }

    /**
     * null を空文字へ変換する。
     *
     * @param value 値
     * @return 変換後の値
     */
    private String nullToEmpty(final String value) {
        return value == null ? "" : value;
    }
}

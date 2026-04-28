package jp.co.shimizutdev.phoneorderapi.presentation.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCannotBeCancelledException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderNotFoundException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderVersionConflictException;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.InvalidPersistedOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * API例外ハンドラ
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * ログ
     */
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * リクエストボディの Bean Validation エラーを 400 Bad Request に変換する。
     *
     * @param ex      バリデーション例外
     * @param request HTTP リクエスト
     * @return バリデーションエラーを表す API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException ex,
        final HttpServletRequest request) {

        logHandledException(ApiErrorResponseMessages.VALIDATION_ERROR, request, ex);

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ApiErrorResponseMessages.VALIDATION_ERROR,
            request,
            ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toValidationError)
                .toList()
        );
    }

    /**
     * パラメータなどの制約違反を 400 Bad Request に変換する。
     *
     * @param ex      制約違反例外
     * @param request HTTP リクエスト
     * @return バリデーションエラーを表す API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
        final ConstraintViolationException ex,
        final HttpServletRequest request) {

        logHandledException(ApiErrorResponseMessages.VALIDATION_ERROR, request, ex);

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ApiErrorResponseMessages.VALIDATION_ERROR,
            request,
            ex.getConstraintViolations()
                .stream()
                .map(this::toValidationError)
                .toList()
        );
    }

    /**
     * 不正な引数を表す {@link IllegalArgumentException} を 400 Bad Request に変換する。
     *
     * @param ex      不正引数例外
     * @param request HTTP リクエスト
     * @return API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
        final IllegalArgumentException ex,
        final HttpServletRequest request) {

        String message = resolveMessage(ex.getMessage(), ApiErrorResponseMessages.VALIDATION_ERROR);
        logHandledException(message, request, ex);

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            message,
            request,
            List.of()
        );
    }

    /**
     * JSON の構文不正や型不一致を 400 Bad Request に変換する。
     *
     * @param ex      メッセージ変換例外
     * @param request HTTP リクエスト
     * @return API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(
        final HttpMessageNotReadableException ex,
        final HttpServletRequest request) {

        logHandledException(ApiErrorResponseMessages.INVALID_REQUEST_BODY, request, ex);

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ApiErrorResponseMessages.INVALID_REQUEST_BODY,
            request,
            List.of()
        );
    }

    /**
     * 注文キャンセル不可の業務例外を 409 Conflict に変換する。
     *
     * @param ex      注文キャンセル不可例外
     * @param request HTTP リクエスト
     * @return API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(OrderCannotBeCancelledException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderCannotBeCancelledException(
        final OrderCannotBeCancelledException ex,
        final HttpServletRequest request) {

        logHandledException(ApiErrorResponseMessages.ORDER_CANNOT_BE_CANCELLED, request, ex);

        return buildErrorResponse(
            HttpStatus.CONFLICT,
            ApiErrorResponseMessages.ORDER_CANNOT_BE_CANCELLED,
            request,
            List.of()
        );
    }

    /**
     * 注文の楽観ロック競合を 409 Conflict に変換する。
     *
     * @param ex      楽観ロック競合例外
     * @param request HTTP リクエスト
     * @return API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler({OrderVersionConflictException.class, ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<ApiErrorResponse> handleOptimisticLockException(
        final RuntimeException ex,
        final HttpServletRequest request) {

        logHandledException(ApiErrorResponseMessages.ORDER_VERSION_CONFLICT, request, ex);

        return buildErrorResponse(
            HttpStatus.CONFLICT,
            ApiErrorResponseMessages.ORDER_VERSION_CONFLICT,
            request,
            List.of()
        );
    }

    /**
     * 永続化済みデータ不整合を 500 Internal Server Error に変換する。
     *
     * @param ex      永続化済みデータ不整合例外
     * @param request HTTP リクエスト
     * @return API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(InvalidPersistedOrderException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPersistedOrderException(
        final InvalidPersistedOrderException ex,
        final HttpServletRequest request) {

        logHandledUnexpectedException(
            resolveMessage(ex.getMessage(), ApiErrorResponseMessages.INTERNAL_SERVER_ERROR),
            request,
            ex
        );

        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ApiErrorResponseMessages.INTERNAL_SERVER_ERROR,
            request,
            List.of()
        );
    }

    /**
     * コントローラから明示的に送出された 404 をエラーレスポンスへ変換する。
     *
     * @param ex      注文未存在例外
     * @param request HTTP リクエスト
     * @return API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderNotFoundException(
        final OrderNotFoundException ex,
        final HttpServletRequest request) {

        logHandledException(ApiErrorResponseMessages.ORDER_NOT_FOUND, request, ex);

        return buildErrorResponse(
            HttpStatus.NOT_FOUND,
            ApiErrorResponseMessages.ORDER_NOT_FOUND,
            request,
            List.of()
        );
    }

    /**
     * コントローラから送出された HTTP ステータス例外をエラーレスポンスへ変換する。
     *
     * @param ex      HTTP ステータス例外
     * @param request HTTP リクエスト
     * @return API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(
        final ResponseStatusException ex,
        final HttpServletRequest request) {

        String message = resolveMessage(ex.getReason(), ApiErrorResponseMessages.REQUEST_FAILED);
        logHandledException(message, request, ex);

        return buildErrorResponse(
            ex.getStatusCode(),
            message,
            request,
            List.of()
        );
    }

    /**
     * 想定外例外を 500 Internal Server Error に変換し、詳細はサーバーログへ記録する。
     *
     * @param ex      想定外例外
     * @param request HTTP リクエスト
     * @return API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
        final Exception ex,
        final HttpServletRequest request) {

        logHandledUnexpectedException(
            ApiErrorResponseMessages.INTERNAL_SERVER_ERROR,
            request,
            ex
        );

        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ApiErrorResponseMessages.INTERNAL_SERVER_ERROR,
            request,
            List.of()
        );
    }

    /**
     * フィールド単位のバリデーションエラーを API 用のエラー表現へ変換する。
     *
     * @param fieldError フィールドエラー
     * @return API 用バリデーションエラー
     */
    private ApiValidationError toValidationError(final FieldError fieldError) {
        return new ApiValidationError(
            fieldError.getField(),
            resolveMessage(fieldError.getDefaultMessage(), ApiErrorResponseMessages.VALIDATION_ERROR)
        );
    }

    /**
     * 制約違反を API 用のエラー表現へ変換する。
     *
     * @param violation 制約違反
     * @return API 用バリデーションエラー
     */
    private ApiValidationError toValidationError(final ConstraintViolation<?> violation) {
        return new ApiValidationError(
            extractViolationField(violation),
            resolveMessage(violation.getMessage(), ApiErrorResponseMessages.VALIDATION_ERROR)
        );
    }

    /**
     * 制約違反のプロパティパスからクライアントへ返却する項目名を抽出する。
     *
     * @param violation 制約違反
     * @return 項目名
     */
    private String extractViolationField(final ConstraintViolation<?> violation) {
        return StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
            .map(Path.Node::getName)
            .filter(Objects::nonNull)
            .reduce((first, second) -> second)
            .orElse(violation.getPropertyPath().toString());
    }

    /**
     * メッセージが null または空の場合にデフォルトメッセージへ置き換える。
     *
     * @param message        変換元メッセージ
     * @param defaultMessage デフォルトメッセージ
     * @return メッセージ
     */
    private String resolveMessage(final String message, final String defaultMessage) {
        return message != null && !message.isBlank()
            ? message
            : defaultMessage;
    }

    /**
     * ハンドリング済み例外をデバッグログに出力する。
     *
     * @param message ログメッセージ
     * @param request HTTP リクエスト
     * @param ex      例外
     */
    private void logHandledException(
        final String message,
        final HttpServletRequest request,
        final Exception ex) {

        log.debug("path={}, message={}", request.getRequestURI(), message, ex);
    }

    /**
     * 想定外またはサーバー内部例外をエラーログに出力する。
     *
     * @param message ログメッセージ
     * @param request HTTP リクエスト
     * @param ex      例外
     */
    private void logHandledUnexpectedException(
        final String message,
        final HttpServletRequest request,
        final Exception ex) {

        log.error("path={}, message={}", request.getRequestURI(), message, ex);
    }

    /**
     * 指定したステータスとメッセージから統一形式のエラーレスポンスを組み立てる。
     *
     * @param statusCode       HTTP ステータス
     * @param message          エラーメッセージ
     * @param request          HTTP リクエスト
     * @param validationErrors バリデーションエラー一覧
     * @return API エラーレスポンス
     */
    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
        final HttpStatusCode statusCode,
        final String message,
        final HttpServletRequest request,
        final List<ApiValidationError> validationErrors) {

        return ResponseEntity.status(statusCode)
            .body(createErrorResponse(
                statusCode,
                message,
                request,
                validationErrors
            ));
    }

    /**
     * エラーレスポンス本体を生成する。
     *
     * @param statusCode       HTTP ステータス
     * @param message          エラーメッセージ
     * @param request          HTTP リクエスト
     * @param validationErrors バリデーションエラー一覧
     * @return エラーレスポンス本体
     */
    private ApiErrorResponse createErrorResponse(
        final HttpStatusCode statusCode,
        final String message,
        final HttpServletRequest request,
        final List<ApiValidationError> validationErrors) {

        return new ApiErrorResponse(
            OffsetDateTime.now(),
            statusCode.value(),
            resolveErrorCode(statusCode),
            message,
            request.getRequestURI(),
            validationErrors
        );
    }

    /**
     * HTTP ステータスコードを API レスポンス用のエラーコード文字列へ変換する。
     *
     * @param statusCode HTTP ステータス
     * @return エラーコード文字列
     */
    private String resolveErrorCode(final HttpStatusCode statusCode) {
        return Optional.ofNullable(HttpStatus.resolve(statusCode.value()))
            .map(HttpStatus::name)
            .orElse("HTTP_" + statusCode.value());
    }
}

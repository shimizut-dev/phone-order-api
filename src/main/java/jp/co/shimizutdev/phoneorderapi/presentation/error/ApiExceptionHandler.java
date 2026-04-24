package jp.co.shimizutdev.phoneorderapi.presentation.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCannotBeCancelledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * API例外ハンドラ
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * リクエストボディの Bean Validation エラーを 400 Bad Request に変換する。
     *
     * @param ex      バリデーション例外
     * @param request HTTP リクエスト
     * @return バリデーションエラーを含む API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException ex,
        final HttpServletRequest request) {

        List<ApiValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::toValidationError)
            .toList();

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ApiErrorResponseMessages.VALIDATION_ERROR,
            request,
            validationErrors
        );
    }

    /**
     * パラメータなどの制約違反を 400 Bad Request に変換する。
     *
     * @param ex      制約違反例外
     * @param request HTTP リクエスト
     * @return バリデーションエラーを含む API エラーレスポンス
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
        final ConstraintViolationException ex,
        final HttpServletRequest request) {

        List<ApiValidationError> validationErrors = ex.getConstraintViolations()
            .stream()
            .map(this::toValidationError)
            .toList();

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            ApiErrorResponseMessages.VALIDATION_ERROR,
            request,
            validationErrors
        );
    }

    /**
     * 業務入力値の不正を表す {@link IllegalArgumentException} を 400 Bad Request に変換する。
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

        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            resolveMessage(ex.getMessage(), ApiErrorResponseMessages.VALIDATION_ERROR),
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

        log.debug("不正なリクエストボディを受信しました。path={}", request.getRequestURI(), ex);

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

        log.debug("キャンセルできない注文に対するキャンセル要求を受信しました。path={}", request.getRequestURI(), ex);

        return buildErrorResponse(
            HttpStatus.CONFLICT,
            ApiErrorResponseMessages.ORDER_CANNOT_BE_CANCELLED,
            request,
            List.of()
        );
    }

    /**
     * コントローラから明示的に送出された HTTP ステータス付き例外をエラーレスポンスへ変換する。
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

        String message = ex.getReason() != null
            ? ex.getReason()
            : ApiErrorResponseMessages.REQUEST_FAILED;

        return buildErrorResponse(
            ex.getStatusCode(),
            resolveMessage(message, ApiErrorResponseMessages.REQUEST_FAILED),
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

        log.error("想定外の例外が発生しました。path={}", request.getRequestURI(), ex);

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
        String field = null;

        for (Path.Node node : violation.getPropertyPath()) {
            if (node.getName() != null) {
                field = node.getName();
            }
        }

        return field != null ? field : violation.getPropertyPath().toString();
    }

    private String resolveMessage(final String message, final String defaultMessage) {
        return message != null && !message.isBlank()
            ? message
            : defaultMessage;
    }

    /**
     * 指定したステータスとメッセージから統一形式のエラーレスポンスを生成する。
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
            .body(createErrorResponse(statusCode, message, request, validationErrors));
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
        HttpStatus resolvedStatus = HttpStatus.resolve(statusCode.value());
        return resolvedStatus != null
            ? resolvedStatus.name()
            : "HTTP_" + statusCode.value();
    }
}

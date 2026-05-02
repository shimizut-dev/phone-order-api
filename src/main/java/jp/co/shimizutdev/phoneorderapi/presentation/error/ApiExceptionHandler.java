package jp.co.shimizutdev.phoneorderapi.presentation.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCannotBeCancelledException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderNotFoundException;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderVersionConflictException;
import jp.co.shimizutdev.phoneorderapi.infrastructure.config.InvalidAuditorException;
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

/** API例外ハンドラ */
@RestControllerAdvice
public class ApiExceptionHandler {

  /** ロガー */
  private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

  /**
   * リクエストボディの Bean Validation エラーを 400 Bad Request に変換する
   *
   * @param ex バリデーション例外
   * @param request HTTP リクエスト
   * @return バリデーションエラーを含む API エラーレスポンス
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException ex, final HttpServletRequest request) {

    logHandledException(ApiErrorMessages.VALIDATION_ERROR, request, ex);

    return buildErrorResponse(
        HttpStatus.BAD_REQUEST,
        ApiErrorMessages.VALIDATION_ERROR,
        request,
        ex.getBindingResult().getFieldErrors().stream().map(this::toValidationError).toList());
  }

  /**
   * パラメータなどの制約違反を 400 Bad Request に変換する
   *
   * @param ex 制約違反例外
   * @param request HTTP リクエスト
   * @return バリデーションエラーを含む API エラーレスポンス
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
      final ConstraintViolationException ex, final HttpServletRequest request) {

    logHandledException(ApiErrorMessages.VALIDATION_ERROR, request, ex);

    return buildErrorResponse(
        HttpStatus.BAD_REQUEST,
        ApiErrorMessages.VALIDATION_ERROR,
        request,
        ex.getConstraintViolations().stream().map(this::toValidationError).toList());
  }

  /**
   * API入力値エラーを 400 Bad Request に変換する
   *
   * @param ex API入力値エラー
   * @param request HTTP リクエスト
   * @return API エラーレスポンス
   */
  @ExceptionHandler(ApiBadRequestException.class)
  public ResponseEntity<ApiErrorResponse> handleApiBadRequestException(
      final ApiBadRequestException ex, final HttpServletRequest request) {

    String message = resolveMessage(ex.getMessage(), ApiErrorMessages.VALIDATION_ERROR);

    logHandledException(message, request, ex);

    return buildErrorResponse(
        HttpStatus.BAD_REQUEST,
        ApiErrorMessages.VALIDATION_ERROR,
        request,
        List.of(new ApiValidationError(ex.getField(), message)));
  }

  /**
   * JSON 形式不正などの読み取りエラーを 400 Bad Request に変換する
   *
   * @param ex メッセージ読み取り例外
   * @param request HTTP リクエスト
   * @return API エラーレスポンス
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(
      final HttpMessageNotReadableException ex, final HttpServletRequest request) {

    logHandledException(ApiErrorMessages.INVALID_REQUEST_BODY, request, ex);

    return buildErrorResponse(
        HttpStatus.BAD_REQUEST, ApiErrorMessages.INVALID_REQUEST_BODY, request, List.of());
  }

  /**
   * 監査ユーザー不正例外を 400 Bad Request に変換する
   *
   * @param ex 監査ユーザー不正例外
   * @param request HTTP リクエスト
   * @return API エラーレスポンス
   */
  @ExceptionHandler(InvalidAuditorException.class)
  public ResponseEntity<ApiErrorResponse> handleInvalidAuditorException(
      final InvalidAuditorException ex, final HttpServletRequest request) {

    logHandledException(ApiErrorMessages.INVALID_AUDITOR, request, ex);

    return buildErrorResponse(
        HttpStatus.BAD_REQUEST, ApiErrorMessages.INVALID_AUDITOR, request, List.of());
  }

  /**
   * 注文キャンセル不可例外を 409 Conflict に変換する
   *
   * @param ex 注文キャンセル不可例外
   * @param request HTTP リクエスト
   * @return API エラーレスポンス
   */
  @ExceptionHandler(OrderCannotBeCancelledException.class)
  public ResponseEntity<ApiErrorResponse> handleOrderCannotBeCancelledException(
      final OrderCannotBeCancelledException ex, final HttpServletRequest request) {

    logHandledException(ApiErrorMessages.ORDER_CANNOT_BE_CANCELLED, request, ex);

    return buildErrorResponse(
        HttpStatus.CONFLICT, ApiErrorMessages.ORDER_CANNOT_BE_CANCELLED, request, List.of());
  }

  /**
   * 楽観ロック例外を 409 Conflict に変換する
   *
   * @param ex 楽観ロック例外
   * @param request HTTP リクエスト
   * @return API エラーレスポンス
   */
  @ExceptionHandler({
    OrderVersionConflictException.class,
    ObjectOptimisticLockingFailureException.class
  })
  public ResponseEntity<ApiErrorResponse> handleOptimisticLockException(
      final RuntimeException ex, final HttpServletRequest request) {

    logHandledException(ApiErrorMessages.ORDER_VERSION_CONFLICT, request, ex);

    return buildErrorResponse(
        HttpStatus.CONFLICT, ApiErrorMessages.ORDER_VERSION_CONFLICT, request, List.of());
  }

  /**
   * 永続化データ不整合例外を 500 Internal Server Error に変換する
   *
   * @param ex 永続化データ不整合例外
   * @param request HTTP リクエスト
   * @return API エラーレスポンス
   */
  @ExceptionHandler(InvalidPersistedOrderException.class)
  public ResponseEntity<ApiErrorResponse> handleInvalidPersistedOrderException(
      final InvalidPersistedOrderException ex, final HttpServletRequest request) {

    logHandledUnexpectedException(
        resolveMessage(ex.getMessage(), ApiErrorMessages.INTERNAL_SERVER_ERROR), request, ex);

    return buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ApiErrorMessages.INTERNAL_SERVER_ERROR,
        request,
        List.of());
  }

  /**
   * 注文未検出例外を 404 Not Found に変換する
   *
   * @param ex 注文未検出例外
   * @param request HTTP リクエスト
   * @return API エラーレスポンス
   */
  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleOrderNotFoundException(
      final OrderNotFoundException ex, final HttpServletRequest request) {

    logHandledException(ApiErrorMessages.ORDER_NOT_FOUND, request, ex);

    return buildErrorResponse(
        HttpStatus.NOT_FOUND, ApiErrorMessages.ORDER_NOT_FOUND, request, List.of());
  }

  /**
   * 予期しない例外を 500 Internal Server Error に変換する
   *
   * @param ex 予期しない例外
   * @param request HTTP リクエスト
   * @return API エラーレスポンス
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
      final Exception ex, final HttpServletRequest request) {

    logHandledUnexpectedException(ApiErrorMessages.INTERNAL_SERVER_ERROR, request, ex);

    return buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ApiErrorMessages.INTERNAL_SERVER_ERROR,
        request,
        List.of());
  }

  /**
   * フィールドエラーを API 用のバリデーションエラーへ変換する
   *
   * @param fieldError フィールドエラー
   * @return API 用バリデーションエラー
   */
  private ApiValidationError toValidationError(final FieldError fieldError) {
    return new ApiValidationError(
        fieldError.getField(),
        resolveMessage(fieldError.getDefaultMessage(), ApiErrorMessages.VALIDATION_ERROR));
  }

  /**
   * 制約違反を API 用のバリデーションエラーへ変換する
   *
   * @param violation 制約違反
   * @return API 用バリデーションエラー
   */
  private ApiValidationError toValidationError(final ConstraintViolation<?> violation) {
    return new ApiValidationError(
        extractViolationField(violation),
        resolveMessage(violation.getMessage(), ApiErrorMessages.VALIDATION_ERROR));
  }

  /**
   * 制約違反のプロパティパスからクライアント向けの項目名を抽出する
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
   * メッセージが null または空白の場合にデフォルトメッセージを返す
   *
   * @param message 例外メッセージ
   * @param defaultMessage デフォルトメッセージ
   * @return 解決済みメッセージ
   */
  private String resolveMessage(final String message, final String defaultMessage) {
    return message != null && !message.isBlank() ? message : defaultMessage;
  }

  /**
   * 既知のハンドリング済み例外をデバッグログへ出力する
   *
   * @param message ログメッセージ
   * @param request HTTP リクエスト
   * @param ex 例外
   */
  private void logHandledException(
      final String message, final HttpServletRequest request, final Exception ex) {

    if (log.isDebugEnabled()) {
      log.debug(
          "path={}, message={}, exceptionMessage={}",
          request.getRequestURI(),
          message,
          resolveMessage(ex.getMessage(), message),
          ex);
    }
  }

  /**
   * 予期しない例外をエラーログへ出力する
   *
   * @param message ログメッセージ
   * @param request HTTP リクエスト
   * @param ex 例外
   */
  private void logHandledUnexpectedException(
      final String message, final HttpServletRequest request, final Exception ex) {

    if (log.isErrorEnabled()) {
      log.error(
          "path={}, message={}, exceptionMessage={}",
          request.getRequestURI(),
          message,
          resolveMessage(ex.getMessage(), message),
          ex);
    }
  }

  /**
   * ステータスとメッセージから統一形式のエラーレスポンスを構築する
   *
   * @param statusCode HTTP ステータス
   * @param message エラーメッセージ
   * @param request HTTP リクエスト
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
   * API エラーレスポンス本体を生成する
   *
   * @param statusCode HTTP ステータス
   * @param message エラーメッセージ
   * @param request HTTP リクエスト
   * @param validationErrors バリデーションエラー一覧
   * @return API エラーレスポンス本体
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
        validationErrors);
  }

  /**
   * HTTP ステータスから API レスポンス用のエラーコードを解決する
   *
   * @param statusCode HTTP ステータス
   * @return エラーコード
   */
  private String resolveErrorCode(final HttpStatusCode statusCode) {
    return Optional.ofNullable(HttpStatus.resolve(statusCode.value()))
        .map(HttpStatus::name)
        .orElse("HTTP_" + statusCode.value());
  }
}

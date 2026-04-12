package jp.co.shimizutdev.phoneorderapi.presentation.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
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
 * グローバル例外ハンドラ
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * バリデーションエラーを処理
     *
     * @param ex      MethodArgumentNotValidException
     * @param request HTTPリクエスト
     * @return APIエラーレスポンス
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException ex,
        final HttpServletRequest request) {

        List<ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::toApiValidationError)
            .toList();

        ErrorResponse response = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            "入力値が不正です。",
            request,
            validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 制約違反エラーを処理
     *
     * @param ex      ConstraintViolationException
     * @param request HTTPリクエスト
     * @return APIエラーレスポンス
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
        final ConstraintViolationException ex,
        final HttpServletRequest request) {

        List<ValidationError> validationErrors = ex.getConstraintViolations()
            .stream()
            .map(violation -> ValidationError.create(
                violation.getPropertyPath().toString(),
                violation.getMessage()))
            .toList();

        ErrorResponse response = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            "入力値が不正です。",
            request,
            validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * IllegalArgumentExceptionを処理
     *
     * @param ex      IllegalArgumentException
     * @param request HTTPリクエスト
     * @return APIエラーレスポンス
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        final IllegalArgumentException ex,
        final HttpServletRequest request) {

        ErrorResponse response = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request,
            List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * JSON解析エラーを処理
     *
     * @param request HTTPリクエスト
     * @return APIエラーレスポンス
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        final HttpServletRequest request) {

        ErrorResponse response = createErrorResponse(
            HttpStatus.BAD_REQUEST,
            "リクエストボディの形式が不正です。",
            request,
            List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * レスポンスステータス例外を処理
     *
     * @param ex      ResponseStatusException
     * @param request HTTPリクエスト
     * @return APIエラーレスポンス
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
        ResponseStatusException ex,
        HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String message = ex.getReason() != null ? ex.getReason() : "リクエスト処理に失敗しました。";

        ErrorResponse response = createErrorResponse(status, message, request, List.of());
        return ResponseEntity.status(status).body(response);
    }

    /**
     * 想定外例外を処理
     *
     * @param request HTTPリクエスト
     * @return APIエラーレスポンス
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
        final HttpServletRequest request) {

        ErrorResponse response = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "サーバー内部でエラーが発生しました。",
            request,
            List.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 項目エラーをバリデーションエラーへ変換
     *
     * @param fieldError 項目エラー
     * @return バリデーションエラー
     */
    private ValidationError toApiValidationError(final FieldError fieldError) {
        return ValidationError.create(
            fieldError.getField(),
            fieldError.getDefaultMessage()
        );
    }

    /**
     * エラーレスポンスを生成
     *
     * @param status           HTTPステータス
     * @param message          メッセージ
     * @param request          HTTPリクエスト
     * @param validationErrors バリデーションエラー
     * @return エラーレスポンス
     */
    private ErrorResponse createErrorResponse(
        final HttpStatus status,
        final String message,
        final HttpServletRequest request,
        final List<ValidationError> validationErrors) {

        return ErrorResponse.create(
            OffsetDateTime.now(),
            status.value(),
            status.name(),
            message,
            request.getRequestURI(),
            validationErrors
        );
    }
}

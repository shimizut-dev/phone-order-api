package jp.co.shimizutdev.phoneorderapi.presentation.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * エラーレスポンス
 */
@Getter
@ToString
@EqualsAndHashCode
public class ErrorResponse {

    /**
     * 発生日時
     */
    private final OffsetDateTime timestamp;

    /**
     * HTTPステータス
     */
    private final int status;

    /**
     * エラー種別
     */
    private final String error;

    /**
     * メッセージ
     */
    private final String message;

    /**
     * リクエストパス
     */
    private final String path;

    /**
     * バリデーションエラーリスト
     */
    private final List<ValidationError> validationErrors;

    /**
     * コンストラクタ
     *
     * @param timestamp        発生日時
     * @param status           HTTPステータス
     * @param error            エラー種別
     * @param message          メッセージ
     * @param path             リクエストパス
     * @param validationErrors バリデーションエラーリスト
     */
    private ErrorResponse(
        final OffsetDateTime timestamp,
        final int status,
        final String error,
        final String message,
        final String path,
        final List<ValidationError> validationErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.validationErrors = validationErrors;
    }

    /**
     * エラーレスポンスを生成する
     *
     * @param timestamp        発生日時
     * @param status           HTTPステータス
     * @param error            エラー種別
     * @param message          メッセージ
     * @param path             リクエストパス
     * @param validationErrors バリデーションエラーリスト
     */
    public static ErrorResponse create(
        final OffsetDateTime timestamp,
        final int status,
        final String error,
        final String message,
        final String path,
        final List<ValidationError> validationErrors) {
        return new ErrorResponse(
            timestamp,
            status,
            error,
            message,
            path,
            validationErrors
        );
    }
}

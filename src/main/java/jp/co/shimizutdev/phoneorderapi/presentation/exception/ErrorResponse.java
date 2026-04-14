package jp.co.shimizutdev.phoneorderapi.presentation.exception;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * エラーレスポンス
 *
 * @param timestamp        発生日時
 * @param status           HTTPステータス
 * @param error            エラー種別
 * @param message          メッセージ
 * @param path             リクエストパス
 * @param validationErrors バリデーションエラー一覧
 */
public record ErrorResponse(
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<ValidationError> validationErrors
) {
}



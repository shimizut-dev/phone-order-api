package jp.co.shimizutdev.phoneorderapi.presentation.error;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * APIエラーレスポンス
 *
 * @param timestamp        エラー発生日時
 * @param status           HTTP ステータスコード
 * @param error            エラーコード
 * @param message          エラーメッセージ
 * @param path             リクエストパス
 * @param validationErrors バリデーションエラー一覧
 */
public record ApiErrorResponse(
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<ApiValidationError> validationErrors
) {

    /**
     * コンストラクタ
     *
     * <p>バリデーションエラー一覧は null を空リストに補正し、不変リストとして保持する</p>
     */
    public ApiErrorResponse {
        validationErrors = validationErrors == null
            ? List.of()
            : List.copyOf(validationErrors);
    }
}

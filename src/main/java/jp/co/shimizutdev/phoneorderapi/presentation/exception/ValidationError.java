package jp.co.shimizutdev.phoneorderapi.presentation.exception;

/**
 * バリデーションエラー
 *
 * @param field   項目
 * @param message メッセージ
 */
public record ValidationError(
    String field,
    String message
) {
}

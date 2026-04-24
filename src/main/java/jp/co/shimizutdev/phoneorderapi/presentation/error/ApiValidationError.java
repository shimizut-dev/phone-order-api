package jp.co.shimizutdev.phoneorderapi.presentation.error;

/**
 * APIバリデーションエラー
 *
 * @param field   エラー対象項目
 * @param message エラーメッセージ
 */
public record ApiValidationError(
    String field,
    String message
) {
}

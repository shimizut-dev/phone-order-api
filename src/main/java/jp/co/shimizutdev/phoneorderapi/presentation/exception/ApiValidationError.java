package jp.co.shimizutdev.phoneorderapi.presentation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * APIバリデーションエラー
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiValidationError {

    /**
     * 項目名
     */
    private final String field;

    /**
     * エラーメッセージ
     */
    private final String message;
}

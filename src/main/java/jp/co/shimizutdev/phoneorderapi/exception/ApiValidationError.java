package jp.co.shimizutdev.phoneorderapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * バリデーションエラー
 */
@Getter
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

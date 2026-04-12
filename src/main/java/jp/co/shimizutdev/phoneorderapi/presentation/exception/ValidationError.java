package jp.co.shimizutdev.phoneorderapi.presentation.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * バリデーションエラー
 */
@Getter
@ToString
@EqualsAndHashCode
public class ValidationError {

    /**
     * 項目名
     */
    private final String field;

    /**
     * メッセージ
     */
    private final String message;

    /**
     * コンストラクタ
     *
     * @param field   項目
     * @param message メッセージ
     */
    private ValidationError(final String field, final String message) {
        this.field = field;
        this.message = message;
    }

    /**
     * バリデーションエラーを生成する
     *
     * @param field   項目
     * @param message メッセージ
     */
    public static ValidationError create(final String field, final String message) {
        return new ValidationError(
            field,
            message
        );
    }
}

package jp.co.shimizutdev.phoneorderapi.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * APIエラーレスポンス
 */
@Getter
@Builder
public class ApiErrorResponse {

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
     * バリデーションエラーList
     */
    private final List<ApiValidationError> validationErrors;
}

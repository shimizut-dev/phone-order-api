package jp.co.shimizutdev.phoneorderapi.presentation.exception;

/**
 * APIエラーメッセージ定数
 */
public final class ApiErrorMessageConstants {

    /**
     * 入力値不正メッセージ
     */
    public static final String VALIDATION_ERROR = "入力値が不正です。";

    /**
     * リクエストボディ形式不正メッセージ
     */
    public static final String INVALID_REQUEST_BODY = "リクエストボディの形式が不正です。";

    /**
     * リクエスト処理失敗メッセージ
     */
    public static final String REQUEST_FAILED = "リクエスト処理に失敗しました。";

    /**
     * サーバー内部エラーメッセージ
     */
    public static final String INTERNAL_SERVER_ERROR = "サーバー内部でエラーが発生しました。";

    /**
     * インスタンス化を防止する。
     */
    private ApiErrorMessageConstants() {
    }
}

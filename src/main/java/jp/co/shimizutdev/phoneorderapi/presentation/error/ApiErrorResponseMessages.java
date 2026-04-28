package jp.co.shimizutdev.phoneorderapi.presentation.error;

/**
 * APIエラーレスポンスメッセージ
 */
public final class ApiErrorResponseMessages {

    /**
     * バリデーションエラー時のメッセージ。
     */
    public static final String VALIDATION_ERROR = "入力値が不正です。";

    /**
     * リクエストボディの形式が不正な場合のメッセージ。
     */
    public static final String INVALID_REQUEST_BODY = "リクエストボディの形式が不正です。";

    /**
     * リクエスト処理に失敗した場合のメッセージ。
     */
    public static final String REQUEST_FAILED = "リクエスト処理に失敗しました。";

    /**
     * サーバー内部でエラーが発生した場合のメッセージ。
     */
    public static final String INTERNAL_SERVER_ERROR = "サーバー内部でエラーが発生しました。";

    /**
     * 注文が見つからない場合のメッセージ。
     */
    public static final String ORDER_NOT_FOUND = "注文が見つかりません。";

    /**
     * 注文をキャンセルできない場合のメッセージ。
     */
    public static final String ORDER_CANNOT_BE_CANCELLED = "注文をキャンセルできません。";

    /**
     * 注文の楽観的ロック競合時のメッセージ。
     */
    public static final String ORDER_VERSION_CONFLICT = "注文が他の更新で変更されました。最新データを取得してから再実行してください。";

    /**
     * インスタンス化を防止する。
     */
    private ApiErrorResponseMessages() {
    }
}

package jp.co.shimizutdev.phoneorderapi.infrastructure.config;

/**
 * 監査ユーザー不正例外。
 * <p>
 * リクエストヘッダーから解決した監査ユーザーが DB の監査項目へ保存できない場合に送出する。
 */
public class InvalidAuditorException extends RuntimeException {

    /**
     * コンストラクタ
     *
     * @param message 例外メッセージ
     */
    public InvalidAuditorException(final String message) {
        super(message);
    }
}

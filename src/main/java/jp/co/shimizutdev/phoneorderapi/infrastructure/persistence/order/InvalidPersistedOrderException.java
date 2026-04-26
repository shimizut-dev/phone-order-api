package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

/**
 * 永続化済み注文データがドメイン再構築できない場合の例外
 */
public class InvalidPersistedOrderException extends RuntimeException {

    /**
     * コンストラクタ
     *
     * @param message メッセージ
     */
    public InvalidPersistedOrderException(final String message) {
        super(message);
    }
}

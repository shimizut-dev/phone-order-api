package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

/**
 * 永続化済み注文データ不整合メッセージ
 */
public final class InvalidPersistedOrderMessages {

    /**
     * 注文ID不正メッセージ
     */
    public static final String INVALID_ORDER_ID = "永続化済み注文データの注文IDが不正です。";

    /**
     * 注文コード不正メッセージ
     */
    public static final String INVALID_ORDER_CODE = "永続化済み注文データの注文コードが不正です。";

    /**
     * 注文日時不正メッセージ
     */
    public static final String INVALID_ORDERED_AT = "永続化済み注文データの注文日時が不正です。";

    /**
     * 注文ステータス不正メッセージ
     */
    public static final String INVALID_ORDER_STATUS = "永続化済み注文データの注文ステータスが不正です。";

    /**
     * インスタンス化防止
     */
    private InvalidPersistedOrderMessages() {
    }
}

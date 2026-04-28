package jp.co.shimizutdev.phoneorderapi.domain.order;

/**
 * 注文楽観的ロック競合例外
 */
public class OrderVersionConflictException extends RuntimeException {

    /**
     * キャンセル要求時のバージョン競合を表すログ用メッセージテンプレート。
     */
    private static final String CANCEL_MESSAGE_TEMPLATE =
        "注文のバージョンが競合しています: orderId=%s, orderCode=%s, currentVersion=%d, requestedVersion=%d";

    /**
     * 更新時のバージョン競合を表すログ用メッセージテンプレート。
     */
    private static final String UPDATE_MESSAGE_TEMPLATE =
        "注文のバージョンが競合しています: orderId=%s, orderCode=%s, version=%d";

    /**
     * 指定されたメッセージを持つ例外を生成する。
     *
     * @param message 例外メッセージ
     */
    private OrderVersionConflictException(final String message) {
        super(message);
    }

    /**
     * 要求バージョンと現在バージョンが一致しない場合の例外を生成する。
     *
     * @param order            バージョン競合が発生した注文
     * @param requestedVersion 要求されたバージョン
     * @return 注文バージョン競合例外
     */
    public static OrderVersionConflictException byOrder(
        final Order order,
        final Version requestedVersion) {

        return new OrderVersionConflictException(
            CANCEL_MESSAGE_TEMPLATE.formatted(
                order.getOrderId().getValue(),
                order.getOrderCode().getValue(),
                order.getVersion().getValue(),
                requestedVersion.getValue()
            )
        );
    }

    /**
     * 永続化層で注文更新対象が見つからない場合の例外を生成する。
     *
     * @param order 更新対象の注文
     * @return 注文バージョン競合例外
     */
    public static OrderVersionConflictException byOrderForUpdate(final Order order) {
        return new OrderVersionConflictException(
            UPDATE_MESSAGE_TEMPLATE.formatted(
                order.getOrderId().getValue(),
                order.getOrderCode().getValue(),
                order.getVersion().getValue()
            )
        );
    }
}

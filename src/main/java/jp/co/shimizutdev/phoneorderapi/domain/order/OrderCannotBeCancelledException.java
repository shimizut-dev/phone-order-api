package jp.co.shimizutdev.phoneorderapi.domain.order;

/**
 * 注文キャンセル不可例外
 */
public class OrderCannotBeCancelledException extends RuntimeException {

    /**
     * 注文キャンセル不可時のログ用メッセージテンプレート。
     */
    private static final String MESSAGE_TEMPLATE =
        "注文をキャンセルできません: orderId=%s, orderCode=%s, status=%s";

    /**
     * 指定されたメッセージを持つ例外を生成する。
     *
     * @param message 例外メッセージ
     */
    private OrderCannotBeCancelledException(final String message) {
        super(message);
    }

    /**
     * 注文の状態によりキャンセルできない場合の例外を生成する。
     *
     * @param order キャンセルできない注文
     * @return 注文キャンセル不可例外
     */
    public static OrderCannotBeCancelledException byOrder(final Order order) {
        return new OrderCannotBeCancelledException(
            MESSAGE_TEMPLATE.formatted(
                order.getOrderId().getValue(),
                order.getOrderCode().getValue(),
                order.getOrderStatus().name()
            )
        );
    }
}

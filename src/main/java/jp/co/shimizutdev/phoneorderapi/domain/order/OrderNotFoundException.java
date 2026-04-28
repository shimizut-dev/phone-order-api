package jp.co.shimizutdev.phoneorderapi.domain.order;

/**
 * 注文未存在例外
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * 注文未存在時のログ用メッセージテンプレート。
     */
    private static final String MESSAGE_TEMPLATE = "注文が見つかりません: orderCode=%s";

    /**
     * 指定されたメッセージを持つ例外を生成する。
     *
     * @param message 例外メッセージ
     */
    private OrderNotFoundException(final String message) {
        super(message);
    }

    /**
     * 注文コードで注文が見つからない場合の例外を生成する。
     *
     * @param orderCode 見つからなかった注文コード
     * @return 注文未存在例外
     */
    public static OrderNotFoundException byOrderCode(final String orderCode) {
        return new OrderNotFoundException(MESSAGE_TEMPLATE.formatted(orderCode));
    }
}

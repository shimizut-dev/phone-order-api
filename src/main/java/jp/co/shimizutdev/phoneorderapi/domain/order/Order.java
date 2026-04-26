package jp.co.shimizutdev.phoneorderapi.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * 注文
 */
@Getter
@ToString
@EqualsAndHashCode
public class Order {

    /**
     * 注文ID
     */
    private final OrderId orderId;

    /**
     * 注文コード
     */
    private final OrderCode orderCode;

    /**
     * 注文日時
     */
    private final OrderedAt orderedAt;

    /**
     * 注文ステータス
     */
    private final OrderStatus orderStatus;

    /**
     * コンストラクタ
     *
     * @param orderId     注文ID
     * @param orderCode   注文コード
     * @param orderedAt   注文日時
     * @param orderStatus 注文ステータス
     */
    private Order(
        final OrderId orderId,
        final OrderCode orderCode,
        final OrderedAt orderedAt,
        final OrderStatus orderStatus) {
        this.orderId = Objects.requireNonNull(orderId, "注文IDは必須です。");
        this.orderCode = Objects.requireNonNull(orderCode, "注文コードは必須です。");
        this.orderedAt = Objects.requireNonNull(orderedAt, "注文日時は必須です。");
        this.orderStatus = Objects.requireNonNull(orderStatus, "注文ステータスは必須です。");
    }

    /**
     * 注文を生成する
     *
     * @param orderCode 注文コード
     * @param orderedAt 注文日時
     * @return 注文
     */
    public static Order create(
        final OrderCode orderCode,
        final OrderedAt orderedAt) {
        return new Order(
            OrderId.generate(),
            orderCode,
            orderedAt,
            OrderStatus.RECEIVED
        );
    }

    /**
     * 注文を再構築する
     *
     * @param orderId     注文ID
     * @param orderCode   注文コード
     * @param orderedAt   注文日時
     * @param orderStatus 注文ステータス
     * @return 注文
     */
    public static Order reconstruct(
        final OrderId orderId,
        final OrderCode orderCode,
        final OrderedAt orderedAt,
        final OrderStatus orderStatus) {
        return new Order(
            orderId,
            orderCode,
            orderedAt,
            orderStatus
        );
    }

    /**
     * 注文をキャンセルする
     *
     * @return キャンセル済み注文
     */
    public Order cancel() {
        if (orderStatus == OrderStatus.COMPLETED || orderStatus == OrderStatus.CANCELLED) {
            throw new OrderCannotBeCancelledException();
        }

        return new Order(
            orderId,
            orderCode,
            orderedAt,
            OrderStatus.CANCELLED
        );
    }
}

package jp.co.shimizutdev.phoneorderapi.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 注文ドメインテスト
 */
class OrderTest {

    /**
     * <pre>
     * 注文を登録できること。
     *
     * Given 注文日時と注文コード採番を用意する
     * When 注文を登録する
     * Then 注文コードと注文日時と初期ステータスが設定される
     * </pre>
     */
    @Test
    @DisplayName("注文を登録できること")
    void shouldOfOrder() {
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        OrderCodeGenerator orderCodeGenerator = () -> OrderCode.of("ORD000001");

        Order actual = Order.create(orderedAt, orderCodeGenerator);

        assertEquals(OrderCode.of("ORD000001"), actual.getOrderCode());
        assertEquals(OrderedAt.of(orderedAt), actual.getOrderedAt());
        assertEquals(OrderStatus.RECEIVED, actual.getOrderStatus());
    }

    /**
     * <pre>
     * 注文をキャンセルできること。
     *
     * Given 受付状態の注文を用意する
     * When 注文をキャンセルする
     * Then 注文ステータスがキャンセルになる
     * </pre>
     */
    @Test
    @DisplayName("注文をキャンセルできること")
    void shouldCancelOrder() {
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        Order order = Order.reconstruct(
            OrderId.generate(),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.RECEIVED
        );

        Order actual = order.cancel();

        assertEquals(OrderStatus.CANCELLED, actual.getOrderStatus());
        assertEquals(order.getOrderId(), actual.getOrderId());
        assertEquals(order.getOrderCode(), actual.getOrderCode());
        assertEquals(order.getOrderedAt(), actual.getOrderedAt());
    }

    /**
     * <pre>
     * 完了済み注文はキャンセルできないこと。
     *
     * Given 完了状態の注文を用意する
     * When 注文をキャンセルする
     * Then 注文キャンセル不可例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("完了済み注文はキャンセルできないこと")
    void shouldThrowExceptionWhenCompletedOrderIsCancelled() {
        Order order = Order.reconstruct(
            OrderId.generate(),
            OrderCode.of("ORD000001"),
            OrderedAt.of(OffsetDateTime.parse("2026-04-09T10:15:30+09:00")),
            OrderStatus.COMPLETED
        );

        assertThrows(
            OrderCannotBeCancelledException.class,
            order::cancel
        );
    }
}

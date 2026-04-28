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
    void shouldCreateOrder() {
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        OrderedAt actualOrderedAt = OrderedAt.of(orderedAt);
        OrderCode orderCode = OrderCode.of("ORD000001");

        Order actual = Order.create(orderCode, actualOrderedAt);

        assertEquals(OrderCode.of("ORD000001"), actual.getOrderCode());
        assertEquals(actualOrderedAt, actual.getOrderedAt());
        assertEquals(OrderStatus.RECEIVED, actual.getOrderStatus());
        assertEquals(Version.of(0L), actual.getVersion());
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
            OrderStatus.RECEIVED,
            Version.of(3L)
        );

        Order actual = order.cancel(Version.of(3L));

        assertEquals(OrderStatus.CANCELLED, actual.getOrderStatus());
        assertEquals(order.getOrderId(), actual.getOrderId());
        assertEquals(order.getOrderCode(), actual.getOrderCode());
        assertEquals(order.getOrderedAt(), actual.getOrderedAt());
        assertEquals(Version.of(3L), actual.getVersion());
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
            OrderStatus.COMPLETED,
            Version.of(1L)
        );
        Version requestedVersion = Version.of(1L);

        assertThrows(
            OrderCannotBeCancelledException.class,
            () -> order.cancel(requestedVersion)
        );
    }

    /**
     * <pre>
     * stale version でキャンセルした場合は競合例外が発生すること。
     *
     * Given version が進んだ注文データを用意する
     * When 不一致の version で注文をキャンセルする
     * Then 注文の楽観ロック競合例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("stale version でキャンセルすると競合例外になる")
    void shouldThrowExceptionWhenVersionDoesNotMatch() {
        Order order = Order.reconstruct(
            OrderId.generate(),
            OrderCode.of("ORD000001"),
            OrderedAt.of(OffsetDateTime.parse("2026-04-09T10:15:30+09:00")),
            OrderStatus.RECEIVED,
            Version.of(1L)
        );
        Version requestedVersion = Version.of(2L);

        assertThrows(
            OrderVersionConflictException.class,
            () -> order.cancel(requestedVersion)
        );
    }
}

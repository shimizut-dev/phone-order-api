package jp.co.shimizutdev.phoneorderapi.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}

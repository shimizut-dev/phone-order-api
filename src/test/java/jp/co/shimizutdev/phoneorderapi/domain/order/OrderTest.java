package jp.co.shimizutdev.phoneorderapi.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** 注文ドメインテスト */
class OrderTest {

  /**
   *
   *
   * <pre>
   * Given 注文日時と注文コードを用意する
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

    assertEquals("ORD000001", actual.getOrderCode().getValue());
    assertEquals("2026-04-09T10:15:30+09:00", actual.getOrderedAt().getValue().toString());
    assertEquals("001", actual.getOrderStatus().getCode());
    assertEquals(0L, actual.getVersion().getValue());
  }

  /**
   *
   *
   * <pre>
   * Given 受付状態の注文を用意する
   * When 注文をキャンセルする
   * Then 注文ステータスがキャンセルになる
   * </pre>
   */
  @Test
  @DisplayName("注文をキャンセルできること")
  void shouldCancelOrder() {
    OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
    Order order =
        Order.reconstruct(
            OrderId.of(UUID.fromString("00000000-0000-0000-0000-000000000001")),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.RECEIVED,
            Version.of(3L));

    Order actual = order.cancel(Version.of(3L));

    assertEquals("006", actual.getOrderStatus().getCode());
    assertEquals("00000000-0000-0000-0000-000000000001", actual.getOrderId().getValue().toString());
    assertEquals("ORD000001", actual.getOrderCode().getValue());
    assertEquals("2026-04-09T10:15:30+09:00", actual.getOrderedAt().getValue().toString());
    assertEquals(3L, actual.getVersion().getValue());
  }

  /**
   *
   *
   * <pre>
   * Given 完了状態の注文を用意する
   * When 注文をキャンセルする
   * Then 注文キャンセル不可例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("完了済み注文はキャンセルできないこと")
  void shouldThrowExceptionWhenCompletedOrderIsCancelled() {
    Order order =
        Order.reconstruct(
            OrderId.of(UUID.fromString("00000000-0000-0000-0000-000000000002")),
            OrderCode.of("ORD000001"),
            OrderedAt.of(OffsetDateTime.parse("2026-04-09T10:15:30+09:00")),
            OrderStatus.COMPLETED,
            Version.of(1L));
    Version requestedVersion = Version.of(1L);

    OrderCannotBeCancelledException actual =
        assertThrows(OrderCannotBeCancelledException.class, () -> order.cancel(requestedVersion));

    assertEquals(
        "注文をキャンセルできません: orderId=00000000-0000-0000-0000-000000000002, orderCode=ORD000001,"
            + " status=COMPLETED",
        actual.getMessage());
  }

  /**
   *
   *
   * <pre>
   * Given version が進んだ注文データを用意する
   * When 不一致の version で注文をキャンセルする
   * Then 注文の楽観ロック競合例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("stale version でキャンセルすると競合例外になる")
  void shouldThrowExceptionWhenVersionDoesNotMatch() {
    Order order =
        Order.reconstruct(
            OrderId.of(UUID.fromString("00000000-0000-0000-0000-000000000003")),
            OrderCode.of("ORD000001"),
            OrderedAt.of(OffsetDateTime.parse("2026-04-09T10:15:30+09:00")),
            OrderStatus.RECEIVED,
            Version.of(1L));
    Version requestedVersion = Version.of(2L);

    OrderVersionConflictException actual =
        assertThrows(OrderVersionConflictException.class, () -> order.cancel(requestedVersion));

    assertEquals(
        "注文のバージョンが競合しています: orderId=00000000-0000-0000-0000-000000000003, orderCode=ORD000001,"
            + " currentVersion=1, requestedVersion=2",
        actual.getMessage());
  }
}

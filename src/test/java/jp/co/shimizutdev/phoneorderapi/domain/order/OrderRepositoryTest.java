package jp.co.shimizutdev.phoneorderapi.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import jp.co.shimizutdev.phoneorderapi.domain.common.PageResult;
import jp.co.shimizutdev.phoneorderapi.domain.common.PagingCondition;
import jp.co.shimizutdev.phoneorderapi.infrastructure.config.JpaAuditConfig;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaEntity;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaRepository;
import jp.co.shimizutdev.phoneorderapi.infrastructure.repository.order.OrderRepositoryImpl;
import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/** 注文リポジトリテスト */
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaAuditConfig.class, OrderRepositoryImpl.class})
class OrderRepositoryTest extends AbstractPostgreSQLTest {

  /** 注文リポジトリ */
  @Autowired private OrderRepository orderRepository;

  /** 注文JPAリポジトリ */
  @Autowired private OrderJpaRepository orderJpaRepository;

  /** エンティティマネージャ */
  @Autowired private EntityManager entityManager;

  /** テスト実行前処理 */
  @BeforeEach
  void setUp() {
    orderJpaRepository.deleteAll();
  }

  /** テスト実行後処理 */
  @AfterEach
  void tearDown() {
    orderJpaRepository.deleteAll();
  }

  /**
   *
   *
   * <pre>
   * Given 注文データが複数件登録されている
   * When ページング条件を指定して注文一覧を取得する
   * Then 注文日時降順、同一日時は注文コード降順のページング済み注文一覧とページ情報が返る
   * </pre>
   */
  @Test
  @DisplayName("注文一覧を注文日時降順かつ注文コード降順で取得できること")
  void shouldFindAllOrders() {
    OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
    insertOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000001"), "ORD000001", orderedAt, "001", 0L);
    insertOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000002"), "ORD000002", orderedAt, "002", 1L);

    PageResult<Order> actual = orderRepository.findAll(PagingCondition.of(0, 20));

    assertEquals(2, actual.items().size());
    assertEquals(
        "00000000-0000-0000-0000-000000000002",
        actual.items().getFirst().getOrderId().getValue().toString());
    assertEquals("ORD000002", actual.items().getFirst().getOrderCode().getValue());
    assertEquals(
        "2026-04-09T01:15:30Z",
        actual.items().getFirst().getOrderedAt().getValue().toInstant().toString());
    assertEquals("002", actual.items().getFirst().getOrderStatus().getCode());
    assertEquals(1L, actual.items().getFirst().getVersion().getValue());
    assertEquals(
        "00000000-0000-0000-0000-000000000001",
        actual.items().get(1).getOrderId().getValue().toString());
    assertEquals("ORD000001", actual.items().get(1).getOrderCode().getValue());
    assertEquals(
        "2026-04-09T01:15:30Z",
        actual.items().get(1).getOrderedAt().getValue().toInstant().toString());
    assertEquals("001", actual.items().get(1).getOrderStatus().getCode());
    assertEquals(0L, actual.items().get(1).getVersion().getValue());
    assertEquals(0, actual.page());
    assertEquals(20, actual.size());
    assertEquals(2, actual.totalElements());
    assertEquals(1, actual.totalPages());
    assertFalse(actual.hasNext());
    assertFalse(actual.hasPrevious());
  }

  /**
   *
   *
   * <pre>
   * Given 注文データが3件登録されている
   * When ページング条件を指定して注文一覧を取得する
   * Then 指定ページの注文一覧とページ情報が返る
   * </pre>
   */
  @Test
  @DisplayName("注文一覧をページ指定で取得できること")
  void shouldFindOrdersByPage() {
    insertOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000001"),
        "ORD000001",
        OffsetDateTime.parse("2026-04-09T10:00:00+09:00"),
        "001",
        0L);
    insertOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000002"),
        "ORD000002",
        OffsetDateTime.parse("2026-04-09T11:00:00+09:00"),
        "001",
        0L);
    insertOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000003"),
        "ORD000003",
        OffsetDateTime.parse("2026-04-09T12:00:00+09:00"),
        "001",
        0L);

    PageResult<Order> actual = orderRepository.findAll(PagingCondition.of(1, 2));

    assertEquals(1, actual.items().size());
    assertEquals(
        "00000000-0000-0000-0000-000000000001",
        actual.items().getFirst().getOrderId().getValue().toString());
    assertEquals("ORD000001", actual.items().getFirst().getOrderCode().getValue());
    assertEquals(
        "2026-04-09T01:00:00Z",
        actual.items().getFirst().getOrderedAt().getValue().toInstant().toString());
    assertEquals("001", actual.items().getFirst().getOrderStatus().getCode());
    assertEquals(0L, actual.items().getFirst().getVersion().getValue());
    assertEquals(1, actual.page());
    assertEquals(2, actual.size());
    assertEquals(3, actual.totalElements());
    assertEquals(2, actual.totalPages());
    assertFalse(actual.hasNext());
    assertTrue(actual.hasPrevious());
  }

  /**
   *
   *
   * <pre>
   * Given 注文データが2件登録されている
   * When 総ページ数を超えるページング条件を指定して注文一覧を取得する
   * Then 空の注文一覧と指定ページのページ情報が返る
   * </pre>
   */
  @Test
  @DisplayName("範囲外ページは空ページを返すこと")
  void shouldReturnEmptyPageWhenPageIsOutOfRange() {
    insertOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000001"),
        "ORD000001",
        OffsetDateTime.parse("2026-04-09T10:00:00+09:00"),
        "001",
        0L);
    insertOrder(
        UUID.fromString("00000000-0000-0000-0000-000000000002"),
        "ORD000002",
        OffsetDateTime.parse("2026-04-09T11:00:00+09:00"),
        "001",
        0L);

    PageResult<Order> actual = orderRepository.findAll(PagingCondition.of(2, 2));

    assertEquals(0, actual.items().size());
    assertEquals(2, actual.page());
    assertEquals(2, actual.size());
    assertEquals(2, actual.totalElements());
    assertEquals(1, actual.totalPages());
    assertFalse(actual.hasNext());
    assertTrue(actual.hasPrevious());
  }

  /**
   *
   *
   * <pre>
   * Given 注文データが登録されている
   * When 注文コードで注文を取得する
   * Then 対象注文が返る
   * </pre>
   */
  @Test
  @DisplayName("注文コードで注文を取得できること")
  void shouldFindOrderByOrderCode() {
    UUID orderId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
    insertOrder(orderId, "ORD000001", orderedAt, "001", 2L);

    Optional<Order> actual = orderRepository.findByOrderCode(OrderCode.of("ORD000001"));

    assertTrue(actual.isPresent());
    assertEquals(
        "00000000-0000-0000-0000-000000000001", actual.get().getOrderId().getValue().toString());
    assertEquals("ORD000001", actual.get().getOrderCode().getValue());
    assertEquals(
        "2026-04-09T01:15:30Z", actual.get().getOrderedAt().getValue().toInstant().toString());
    assertEquals("001", actual.get().getOrderStatus().getCode());
    assertEquals(2L, actual.get().getVersion().getValue());
  }

  /**
   *
   *
   * <pre>
   * Given 注文データが登録されていない
   * When 注文コードで注文を取得する
   * Then 空が返る
   * </pre>
   */
  @Test
  @DisplayName("存在しない注文コードの場合に空を返すこと")
  void shouldReturnEmptyWhenOrderCodeDoesNotExist() {
    Optional<Order> actual = orderRepository.findByOrderCode(OrderCode.of("ORD999999"));

    assertTrue(actual.isEmpty());
  }

  /**
   *
   *
   * <pre>
   * Given 登録対象の注文を用意する
   * When 注文を登録する
   * Then 注文が登録される
   * </pre>
   */
  @Test
  @DisplayName("注文を登録できること")
  void shouldCreateOrder() {
    UUID orderId = UUID.fromString("00000000-0000-0000-0000-000000000002");
    OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
    Order order =
        Order.reconstruct(
            OrderId.of(orderId),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.RECEIVED,
            Version.of(0L));

    Order actual = orderRepository.create(order);
    entityManager.clear();

    assertEquals("00000000-0000-0000-0000-000000000002", actual.getOrderId().getValue().toString());
    assertEquals("ORD000001", actual.getOrderCode().getValue());
    assertEquals("2026-04-09T01:15:30Z", actual.getOrderedAt().getValue().toInstant().toString());
    assertEquals("001", actual.getOrderStatus().getCode());
    assertEquals(0L, actual.getVersion().getValue());

    Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findById(orderId);
    assertTrue(savedOrder.isPresent());
    assertEquals("ORD000001", savedOrder.get().getOrderCode());
    assertEquals("001", savedOrder.get().getOrderStatus());
    assertEquals(0L, savedOrder.get().getVersion());
  }

  /**
   *
   *
   * <pre>
   * Given 注文データが登録されている
   * When 注文を更新する
   * Then 注文ステータスが更新される
   * </pre>
   */
  @Test
  @DisplayName("注文を更新できること")
  void shouldUpdateOrder() {
    UUID orderId = UUID.fromString("00000000-0000-0000-0000-000000000003");
    OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
    insertOrder(orderId, "ORD000001", orderedAt, "001", 0L);

    Order order =
        Order.reconstruct(
            OrderId.of(orderId),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.CANCELLED,
            Version.of(0L));

    Order actual = orderRepository.update(order);
    entityManager.clear();

    assertEquals("00000000-0000-0000-0000-000000000003", actual.getOrderId().getValue().toString());
    assertEquals("ORD000001", actual.getOrderCode().getValue());
    assertEquals("2026-04-09T01:15:30Z", actual.getOrderedAt().getValue().toInstant().toString());
    assertEquals("006", actual.getOrderStatus().getCode());
    assertEquals(1L, actual.getVersion().getValue());

    Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findById(orderId);
    assertTrue(savedOrder.isPresent());
    assertEquals("00000000-0000-0000-0000-000000000003", savedOrder.get().getId().toString());
    assertEquals("ORD000001", savedOrder.get().getOrderCode());
    assertEquals("2026-04-09T01:15:30Z", savedOrder.get().getOrderedAt().toInstant().toString());
    assertEquals("006", savedOrder.get().getOrderStatus());
    assertEquals("system", savedOrder.get().getCreatedBy());
    assertEquals("system", savedOrder.get().getUpdatedBy());
    assertEquals(1L, savedOrder.get().getVersion());
  }

  /**
   *
   *
   * <pre>
   * Given version が進んだ注文データが登録されている
   * When 古い version で注文を更新する
   * Then 注文の楽観ロック競合例外が発生する
   * </pre>
   */
  @Test
  @DisplayName("stale version で更新すると競合例外になる")
  void shouldThrowExceptionWhenUpdatingWithStaleVersion() {
    UUID orderId = UUID.fromString("00000000-0000-0000-0000-000000000004");
    OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
    insertOrder(orderId, "ORD000001", orderedAt, "001", 1L);

    Order order =
        Order.reconstruct(
            OrderId.of(orderId),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.CANCELLED,
            Version.of(0L));

    OrderVersionConflictException actual =
        assertThrows(OrderVersionConflictException.class, () -> orderRepository.update(order));

    assertEquals(
        "注文のバージョンが競合しています: orderId=00000000-0000-0000-0000-000000000004, orderCode=ORD000001,"
            + " version=0",
        actual.getMessage());
  }

  /**
   * 注文データをDBへ直接登録する
   *
   * @param orderId 注文ID
   * @param orderCode 注文コード
   * @param orderedAt 注文日時
   * @param orderStatus 注文ステータス
   * @param version バージョン
   */
  private void insertOrder(
      final UUID orderId,
      final String orderCode,
      final OffsetDateTime orderedAt,
      final String orderStatus,
      final long version) {

    //noinspection SqlResolve,SqlNoDataSourceInspection
    entityManager
        .createNativeQuery(
            """
insert into orders (id, order_code, ordered_at, order_status, version, created_by, updated_by)
values (:id, :orderCode, :orderedAt, :orderStatus, :version, 'system', 'system')
""")
        .setParameter("id", orderId)
        .setParameter("orderCode", orderCode)
        .setParameter("orderedAt", orderedAt)
        .setParameter("orderStatus", orderStatus)
        .setParameter("version", version)
        .executeUpdate();
    entityManager.flush();
    entityManager.clear();
  }
}

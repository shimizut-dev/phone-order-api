package jp.co.shimizutdev.phoneorderapi.application.order;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import jp.co.shimizutdev.phoneorderapi.domain.common.PageResult;
import jp.co.shimizutdev.phoneorderapi.domain.common.PagingCondition;
import jp.co.shimizutdev.phoneorderapi.domain.order.*;
import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaEntity;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaRepository;
import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** 注文サービステスト */
@SpringBootTest
@Transactional
class OrderServiceTest extends AbstractPostgreSQLTest {

  /** 注文サービス */
  @Autowired private OrderService orderService;

  /** 注文リポジトリ */
  @Autowired private OrderJpaRepository orderJpaRepository;

  /** エンティティマネージャ */
  @Autowired private EntityManager entityManager;

  /** テスト前処理 */
  @BeforeEach
  void setUp() {
    cleanupOrderTables();
  }

  /** テスト後処理 */
  @AfterEach
  void tearDown() {
    cleanupOrderTables();
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

  /** 注文関連テーブルを初期化する */
  private void cleanupOrderTables() {
    entityManager.createNativeQuery("truncate table orders cascade").executeUpdate();
    entityManager.createNativeQuery("alter sequence order_code_seq restart with 1").executeUpdate();
    entityManager.flush();
    entityManager.clear();
  }

  @Nested
  @DisplayName("getOrderByOrderCode")
  class GetOrderByOrderCode {

    /**
     *
     *
     * <pre>
     * Given 注文データが保存されている
     * When 注文コードで注文を取得する
     * Then 対象注文が取得できる
     * </pre>
     */
    @Test
    @DisplayName("注文コードで注文を取得できること")
    void shouldGetOrderByOrderCode() {
      insertOrder(
          UUID.fromString("00000000-0000-0000-0000-000000000001"),
          "ORD000001",
          OffsetDateTime.parse("2026-04-09T10:15:30+09:00"),
          "001",
          2L);

      Order actual = orderService.getOrderByOrderCode(OrderCode.of("ORD000001"));

      assertEquals(
          "00000000-0000-0000-0000-000000000001", actual.getOrderId().getValue().toString());
      assertEquals("ORD000001", actual.getOrderCode().getValue());
      assertEquals("2026-04-09T01:15:30Z", actual.getOrderedAt().getValue().toInstant().toString());
      assertEquals("001", actual.getOrderStatus().getCode());
      assertEquals(2L, actual.getVersion().getValue());
    }

    /**
     *
     *
     * <pre>
     * Given 対象注文が登録されていない
     * When 注文コードで注文を取得する
     * Then 注文未存在例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("注文コードに対応する注文が存在しない場合に注文未存在例外が発生すること")
    void shouldThrowExceptionWhenOrderDoesNotExist() {
      OrderCode orderCode = OrderCode.of("ORD999999");

      OrderNotFoundException actual =
          assertThrows(
              OrderNotFoundException.class, () -> orderService.getOrderByOrderCode(orderCode));

      assertEquals("注文が見つかりません: orderCode=ORD999999", actual.getMessage());
    }
  }

  @Nested
  @DisplayName("getOrders")
  class GetOrders {

    /**
     *
     *
     * <pre>
     * Given 注文データが複数件保存されている
     * When ページング条件を指定して注文一覧を取得する
     * Then 注文日時降順、同一日時は注文コード降順のページング済み注文一覧とページ情報が返る
     * </pre>
     */
    @Test
    @DisplayName("注文一覧を取得できること")
    void shouldGetOrders() {
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
          "002",
          1L);

      PageResult<Order> actual = orderService.getOrders(PagingCondition.of(0, 20));

      assertEquals(2, actual.items().size());
      assertEquals(
          "00000000-0000-0000-0000-000000000002",
          actual.items().getFirst().getOrderId().getValue().toString());
      assertEquals("ORD000002", actual.items().getFirst().getOrderCode().getValue());
      assertEquals(
          "2026-04-09T02:00:00Z",
          actual.items().getFirst().getOrderedAt().getValue().toInstant().toString());
      assertEquals("002", actual.items().getFirst().getOrderStatus().getCode());
      assertEquals(1L, actual.items().getFirst().getVersion().getValue());
      assertEquals(
          "00000000-0000-0000-0000-000000000001",
          actual.items().get(1).getOrderId().getValue().toString());
      assertEquals("ORD000001", actual.items().get(1).getOrderCode().getValue());
      assertEquals(
          "2026-04-09T01:00:00Z",
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
     * Given ページング条件を用意しない
     * When 注文一覧を取得する
     * Then ページング条件必須例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("ページング条件がnullの場合は例外になること")
    void shouldThrowExceptionWhenPagingConditionIsNull() {
      NullPointerException actual =
          assertThrows(NullPointerException.class, () -> orderService.getOrders(null));

      assertEquals("ページング条件は必須です。", actual.getMessage());
    }
  }

  @Nested
  @DisplayName("createOrder")
  class CreateOrder {

    /**
     *
     *
     * <pre>
     * Given 注文日時を用意する
     * When 注文を登録する
     * Then 注文が保存され注文コードと初期ステータスが設定される
     * </pre>
     */
    @Test
    @DisplayName("注文を登録できること")
    void shouldCreateOrder() {
      Order createdOrder =
          orderService.createOrder(OrderedAt.of(OffsetDateTime.parse("2026-04-09T10:15:30+09:00")));

      assertEquals("ORD000001", createdOrder.getOrderCode().getValue());
      assertEquals("2026-04-09T10:15:30+09:00", createdOrder.getOrderedAt().getValue().toString());
      assertEquals("001", createdOrder.getOrderStatus().getCode());
      assertEquals(0L, createdOrder.getVersion().getValue());

      Optional<OrderJpaEntity> actual =
          orderJpaRepository.findByOrderCode(createdOrder.getOrderCode().getValue());
      assertTrue(actual.isPresent());
      assertEquals("ORD000001", actual.get().getOrderCode());
      assertEquals("2026-04-09T10:15:30+09:00", actual.get().getOrderedAt().toString());
      assertEquals("001", actual.get().getOrderStatus());
      assertEquals(0L, actual.get().getVersion());
    }

    /**
     *
     *
     * <pre>
     * Given X-User-Id を含むリクエストコンテキストを設定する
     * When 注文を作成する
     * Then createdBy と updatedBy にリクエストユーザーが保存される
     * </pre>
     */
    @Test
    @DisplayName("リクエストヘッダーのユーザー情報が監査項目へ反映されること")
    void shouldUseRequestUserForAuditWhenCreatingOrder() {
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.addHeader("X-User-Id", "user-123");
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

      try {
        Order createdOrder =
            orderService.createOrder(
                OrderedAt.of(OffsetDateTime.parse("2026-04-09T10:15:30+09:00")));

        Optional<OrderJpaEntity> actual =
            orderJpaRepository.findByOrderCode(createdOrder.getOrderCode().getValue());
        assertTrue(actual.isPresent());
        assertEquals("ORD000001", actual.get().getOrderCode());
        assertEquals("2026-04-09T10:15:30+09:00", actual.get().getOrderedAt().toString());
        assertEquals("001", actual.get().getOrderStatus());
        assertEquals(0L, actual.get().getVersion());
        assertEquals("user-123", actual.get().getCreatedBy());
        assertEquals("user-123", actual.get().getUpdatedBy());
      } finally {
        RequestContextHolder.resetRequestAttributes();
      }
    }
  }

  @Nested
  @DisplayName("cancelOrder")
  class CancelOrder {

    /**
     *
     *
     * <pre>
     * Given 注文データが保存されている
     * When 注文をキャンセルする
     * Then 注文ステータスがキャンセルで保存される
     * </pre>
     */
    @Test
    @DisplayName("注文をキャンセルできること")
    void shouldCancelOrder() {
      insertOrder(
          UUID.fromString("00000000-0000-0000-0000-000000000001"),
          "ORD000001",
          OffsetDateTime.parse("2026-04-09T10:15:30+09:00"),
          "001",
          0L);

      Order actual = orderService.cancelOrder(OrderCode.of("ORD000001"), Version.of(0L));

      assertEquals(
          "00000000-0000-0000-0000-000000000001", actual.getOrderId().getValue().toString());
      assertEquals("ORD000001", actual.getOrderCode().getValue());
      assertEquals("2026-04-09T01:15:30Z", actual.getOrderedAt().getValue().toInstant().toString());
      assertEquals("006", actual.getOrderStatus().getCode());
      assertEquals(1L, actual.getVersion().getValue());

      Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findByOrderCode("ORD000001");
      assertTrue(savedOrder.isPresent());
      assertEquals("00000000-0000-0000-0000-000000000001", savedOrder.get().getId().toString());
      assertEquals("ORD000001", savedOrder.get().getOrderCode());
      assertEquals("2026-04-09T01:15:30Z", savedOrder.get().getOrderedAt().toInstant().toString());
      assertEquals("006", savedOrder.get().getOrderStatus());
      assertEquals(1L, savedOrder.get().getVersion());
    }

    /**
     *
     *
     * <pre>
     * Given 既存注文と X-User-Id を含むリクエストコンテキストを用意する
     * When 注文をキャンセルする
     * Then updatedBy にリクエストユーザーが保存される
     * </pre>
     */
    @Test
    @DisplayName("リクエストヘッダーのユーザー情報が更新監査項目へ反映されること")
    void shouldUseRequestUserForAuditWhenCancellingOrder() {
      insertOrder(
          UUID.fromString("00000000-0000-0000-0000-000000000001"),
          "ORD000001",
          OffsetDateTime.parse("2026-04-09T10:15:30+09:00"),
          "001",
          0L);
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.addHeader("X-User-Id", "operator-1");
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

      try {
        orderService.cancelOrder(OrderCode.of("ORD000001"), Version.of(0L));

        Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findByOrderCode("ORD000001");
        assertTrue(savedOrder.isPresent());
        assertEquals("00000000-0000-0000-0000-000000000001", savedOrder.get().getId().toString());
        assertEquals("ORD000001", savedOrder.get().getOrderCode());
        assertEquals("006", savedOrder.get().getOrderStatus());
        assertEquals(1L, savedOrder.get().getVersion());
        assertEquals("system", savedOrder.get().getCreatedBy());
        assertEquals("operator-1", savedOrder.get().getUpdatedBy());
      } finally {
        RequestContextHolder.resetRequestAttributes();
      }
    }

    /**
     *
     *
     * <pre>
     * Given キャンセル対象の注文データが保存されていない
     * When 注文をキャンセルする
     * Then 注文未存在例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("未存在の注文はキャンセルできないこと")
    void shouldThrowExceptionWhenCancelTargetDoesNotExist() {
      OrderCode orderCode = OrderCode.of("ORD999999");
      Version version = Version.of(0L);

      OrderNotFoundException actual =
          assertThrows(
              OrderNotFoundException.class, () -> orderService.cancelOrder(orderCode, version));

      assertEquals("注文が見つかりません: orderCode=ORD999999", actual.getMessage());
    }

    /**
     *
     *
     * <pre>
     * Given 完了状態の注文データが保存されている
     * When 注文をキャンセルする
     * Then 注文キャンセル不可例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("完了済み注文はキャンセルできないこと")
    void shouldThrowExceptionWhenCompletedOrderIsCancelled() {
      insertOrder(
          UUID.fromString("00000000-0000-0000-0000-000000000001"),
          "ORD000001",
          OffsetDateTime.parse("2026-04-09T10:15:30+09:00"),
          "005",
          0L);
      OrderCode orderCode = OrderCode.of("ORD000001");
      Version version = Version.of(0L);

      OrderCannotBeCancelledException actual =
          assertThrows(
              OrderCannotBeCancelledException.class,
              () -> orderService.cancelOrder(orderCode, version));

      assertEquals(
          "注文をキャンセルできません: orderId=00000000-0000-0000-0000-000000000001, orderCode=ORD000001,"
              + " status=COMPLETED",
          actual.getMessage());
    }

    /**
     *
     *
     * <pre>
     * Given 対象注文データが保存されている
     * When 不一致のバージョンで注文をキャンセルする
     * Then 注文楽観的ロック競合例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("不一致のバージョンでキャンセルした場合は競合例外が発生すること")
    void shouldThrowExceptionWhenVersionDoesNotMatch() {
      insertOrder(
          UUID.fromString("00000000-0000-0000-0000-000000000002"),
          "ORD000001",
          OffsetDateTime.parse("2026-04-09T10:15:30+09:00"),
          "001",
          0L);
      OrderCode orderCode = OrderCode.of("ORD000001");
      Version version = Version.of(1L);

      OrderVersionConflictException actual =
          assertThrows(
              OrderVersionConflictException.class,
              () -> orderService.cancelOrder(orderCode, version));

      assertEquals(
          "注文のバージョンが競合しています: orderId=00000000-0000-0000-0000-000000000002, orderCode=ORD000001,"
              + " currentVersion=0, requestedVersion=1",
          actual.getMessage());
    }
  }
}

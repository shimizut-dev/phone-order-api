package jp.co.shimizutdev.phoneorderapi.domain.order;

import jakarta.persistence.EntityManager;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 注文リポジトリテスト
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaAuditConfig.class, OrderRepositoryImpl.class})
class OrderRepositoryTest extends AbstractPostgreSQLTest {

    /**
     * 注文リポジトリ
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * 注文JPAリポジトリ
     */
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    /**
     * エンティティマネージャ
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * テスト実行前処理
     */
    @BeforeEach
    void setUp() {
        orderJpaRepository.deleteAll();
    }

    /**
     * テスト実行後処理
     */
    @AfterEach
    void tearDown() {
        orderJpaRepository.deleteAll();
    }

    /**
     * <pre>
     * 注文一覧を取得できること。
     *
     * Given 注文データが複数件登録されている
     * When 注文一覧を取得する
     * Then 登録された注文一覧が返る
     * </pre>
     */
    @Test
    @DisplayName("注文一覧を取得できること")
    void shouldFindAllOrders() {
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        insertOrder(UUID.randomUUID(), "ORD000001", orderedAt, "001", 0L);
        insertOrder(UUID.randomUUID(), "ORD000002", orderedAt, "002", 1L);

        List<Order> actual = orderRepository.findAll();

        assertEquals(2, actual.size());
        assertTrue(actual.stream().anyMatch(order ->
            OrderCode.of("ORD000001").equals(order.getOrderCode()) && Version.of(0L).equals(order.getVersion())));
        assertTrue(actual.stream().anyMatch(order ->
            OrderCode.of("ORD000002").equals(order.getOrderCode()) && Version.of(1L).equals(order.getVersion())));
    }

    /**
     * <pre>
     * 注文コードで注文を取得できること。
     *
     * Given 注文データが登録されている
     * When 注文コードで注文を取得する
     * Then 対象注文が返る
     * </pre>
     */
    @Test
    @DisplayName("注文コードで注文を取得できること")
    void shouldFindOrderByOrderCode() {
        UUID orderId = UUID.randomUUID();
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        insertOrder(orderId, "ORD000001", orderedAt, "001", 2L);

        Optional<Order> actual = orderRepository.findByOrderCode(OrderCode.of("ORD000001"));

        assertTrue(actual.isPresent());
        assertEquals(OrderId.of(orderId), actual.get().getOrderId());
        assertEquals(OrderCode.of("ORD000001"), actual.get().getOrderCode());
        assertEquals(orderedAt.toInstant(), actual.get().getOrderedAt().getValue().toInstant());
        assertEquals(OrderStatus.RECEIVED, actual.get().getOrderStatus());
        assertEquals(Version.of(2L), actual.get().getVersion());
    }

    /**
     * <pre>
     * 存在しない注文コードの場合に空を返すこと。
     *
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
     * <pre>
     * 注文を登録できること。
     *
     * Given 登録対象の注文を用意する
     * When 注文を登録する
     * Then 注文が登録される
     * </pre>
     */
    @Test
    @DisplayName("注文を登録できること")
    void shouldCreateOrder() {
        UUID orderId = UUID.randomUUID();
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        Order order = Order.reconstruct(
            OrderId.of(orderId),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.RECEIVED,
            Version.of(0L)
        );

        Order actual = orderRepository.create(order);
        entityManager.clear();

        assertEquals(OrderId.of(orderId), actual.getOrderId());
        assertEquals(OrderCode.of("ORD000001"), actual.getOrderCode());
        assertEquals(OrderedAt.of(orderedAt), actual.getOrderedAt());
        assertEquals(OrderStatus.RECEIVED, actual.getOrderStatus());
        assertEquals(Version.of(0L), actual.getVersion());

        Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findById(orderId);
        assertTrue(savedOrder.isPresent());
        assertEquals("ORD000001", savedOrder.get().getOrderCode());
        assertEquals("001", savedOrder.get().getOrderStatus());
        assertEquals(0L, savedOrder.get().getVersion());
    }

    /**
     * <pre>
     * 注文を更新できること。
     *
     * Given 注文データが登録されている
     * When 注文を更新する
     * Then 注文ステータスが更新される
     * </pre>
     */
    @Test
    @DisplayName("注文を更新できること")
    void shouldUpdateOrder() {
        UUID orderId = UUID.randomUUID();
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        insertOrder(orderId, "ORD000001", orderedAt, "001", 0L);

        Order order = Order.reconstruct(
            OrderId.of(orderId),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.CANCELLED,
            Version.of(0L)
        );

        Order actual = orderRepository.update(order);
        entityManager.clear();

        assertEquals(OrderId.of(orderId), actual.getOrderId());
        assertEquals(OrderCode.of("ORD000001"), actual.getOrderCode());
        assertEquals(OrderStatus.CANCELLED, actual.getOrderStatus());
        assertEquals(Version.of(1L), actual.getVersion());

        Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findById(orderId);
        assertTrue(savedOrder.isPresent());
        assertEquals("006", savedOrder.get().getOrderStatus());
        assertEquals("system", savedOrder.get().getUpdatedBy());
        assertEquals(1L, savedOrder.get().getVersion());
    }

    /**
     * <pre>
     * stale version で更新した場合に競合例外が発生すること。
     *
     * Given version が進んだ注文データが登録されている
     * When 古い version で注文を更新する
     * Then 注文の楽観ロック競合例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("stale version で更新すると競合例外になる")
    void shouldThrowExceptionWhenUpdatingWithStaleVersion() {
        UUID orderId = UUID.randomUUID();
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        insertOrder(orderId, "ORD000001", orderedAt, "001", 1L);

        Order order = Order.reconstruct(
            OrderId.of(orderId),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.CANCELLED,
            Version.of(0L)
        );

        assertThrows(
            OrderVersionConflictException.class,
            () -> orderRepository.update(order)
        );
    }

    /**
     * 注文データをDBへ直接登録する
     *
     * @param orderId     注文ID
     * @param orderCode   注文コード
     * @param orderedAt   注文日時
     * @param orderStatus 注文ステータス
     * @param version     バージョン
     */
    private void insertOrder(
        final UUID orderId,
        final String orderCode,
        final OffsetDateTime orderedAt,
        final String orderStatus,
        final long version) {

        //noinspection SqlResolve,SqlNoDataSourceInspection
        entityManager.createNativeQuery("""
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

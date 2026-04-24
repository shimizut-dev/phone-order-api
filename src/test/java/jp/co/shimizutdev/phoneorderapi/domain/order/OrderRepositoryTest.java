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
@Import({JpaAuditConfig.class, OrderRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
     * テスト前処理
     */
    @BeforeEach
    void setUp() {
        orderJpaRepository.deleteAll();
    }

    /**
     * テスト後処理
     */
    @AfterEach
    void tearDown() {
        orderJpaRepository.deleteAll();
    }

    /**
     * <pre>
     * 注文一覧を取得できること。
     *
     * Given 注文データが複数件保存されている
     * When 注文一覧を取得する
     * Then 保存された注文一覧が返る
     * </pre>
     */
    @Test
    @DisplayName("注文一覧を取得できること")
    void shouldFindAllOrders() {
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        orderJpaRepository.save(reconstructOrderJpaEntity(UUID.randomUUID(), "ORD000001", orderedAt, "001"));
        orderJpaRepository.save(reconstructOrderJpaEntity(UUID.randomUUID(), "ORD000002", orderedAt, "002"));
        entityManager.flush();
        entityManager.clear();

        List<Order> actual = orderRepository.findAll();

        assertEquals(2, actual.size());
        assertTrue(actual.stream().anyMatch(order -> OrderCode.of("ORD000001").equals(order.getOrderCode())));
        assertTrue(actual.stream().anyMatch(order -> OrderCode.of("ORD000002").equals(order.getOrderCode())));
    }

    /**
     * <pre>
     * 注文コードで注文を取得できること。
     *
     * Given 注文データが保存されている
     * When 注文コードで注文を取得する
     * Then 対象注文が返る
     * </pre>
     */
    @Test
    @DisplayName("注文コードで注文を取得できること")
    void shouldFindOrderByOrderCode() {
        UUID orderId = UUID.randomUUID();
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        orderJpaRepository.save(reconstructOrderJpaEntity(orderId, "ORD000001", orderedAt, "001"));
        entityManager.flush();
        entityManager.clear();

        Optional<Order> actual = orderRepository.findByOrderCode(OrderCode.of("ORD000001"));

        assertTrue(actual.isPresent());
        assertEquals(OrderId.of(orderId), actual.get().getOrderId());
        assertEquals(OrderCode.of("ORD000001"), actual.get().getOrderCode());
        assertEquals(orderedAt.toInstant(), actual.get().getOrderedAt().getValue().toInstant());
        assertEquals(OrderStatus.RECEIVED, actual.get().getOrderStatus());
    }

    /**
     * <pre>
     * 存在しない注文コードの場合は空を返すこと。
     *
     * Given 注文データが保存されていない
     * When 注文コードで注文を取得する
     * Then 空が返る
     * </pre>
     */
    @Test
    @DisplayName("存在しない注文コードの場合は空を返すこと")
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
     * Then 注文が保存される
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
            OrderStatus.RECEIVED
        );

        Order actual = orderRepository.create(order);
        entityManager.flush();
        entityManager.clear();

        assertEquals(OrderId.of(orderId), actual.getOrderId());
        assertEquals(OrderCode.of("ORD000001"), actual.getOrderCode());
        assertEquals(OrderedAt.of(orderedAt), actual.getOrderedAt());
        assertEquals(OrderStatus.RECEIVED, actual.getOrderStatus());

        Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findById(orderId);
        assertTrue(savedOrder.isPresent());
        assertEquals("ORD000001", savedOrder.get().getOrderCode());
        assertEquals("001", savedOrder.get().getOrderStatus());
    }

    /**
     * <pre>
     * 注文を更新できること。
     *
     * Given 注文データが保存されている
     * When 注文を更新する
     * Then 注文ステータスが更新される
     * </pre>
     */
    @Test
    @DisplayName("注文を更新できること")
    void shouldUpdateOrder() {
        UUID orderId = UUID.randomUUID();
        OffsetDateTime orderedAt = OffsetDateTime.parse("2026-04-09T10:15:30+09:00");
        orderJpaRepository.save(reconstructOrderJpaEntity(orderId, "ORD000001", orderedAt, "001"));
        entityManager.flush();
        entityManager.clear();

        Order order = Order.reconstruct(
            OrderId.of(orderId),
            OrderCode.of("ORD000001"),
            OrderedAt.of(orderedAt),
            OrderStatus.CANCELLED
        );

        Order actual = orderRepository.update(order);
        entityManager.flush();
        entityManager.clear();

        assertEquals(OrderId.of(orderId), actual.getOrderId());
        assertEquals(OrderCode.of("ORD000001"), actual.getOrderCode());
        assertEquals(OrderStatus.CANCELLED, actual.getOrderStatus());

        Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findById(orderId);
        assertTrue(savedOrder.isPresent());
        assertEquals("006", savedOrder.get().getOrderStatus());
        assertEquals("system", savedOrder.get().getUpdatedBy());
    }

    /**
     * <pre>
     * 更新対象の注文が存在しない場合は例外が発生すること。
     *
     * Given 注文データが保存されていない
     * When 注文を更新する
     * Then IllegalStateExceptionが発生する
     * </pre>
     */
    @Test
    @DisplayName("更新対象の注文が存在しない場合は例外が発生すること")
    void shouldThrowExceptionWhenUpdateTargetOrderDoesNotExist() {
        Order order = Order.reconstruct(
            OrderId.generate(),
            OrderCode.of("ORD999999"),
            OrderedAt.of(OffsetDateTime.parse("2026-04-09T10:15:30+09:00")),
            OrderStatus.CANCELLED
        );

        IllegalStateException actual = assertThrows(
            IllegalStateException.class,
            () -> orderRepository.update(order)
        );

        assertEquals("更新対象の注文が見つかりません。", actual.getMessage());
    }

    /**
     * 注文JPAエンティティを再構築する
     *
     * @param orderId     注文ID
     * @param orderCode   注文コード
     * @param orderedAt   注文日時
     * @param orderStatus 注文ステータス
     * @return 注文JPAエンティティ
     */
    private OrderJpaEntity reconstructOrderJpaEntity(
        final UUID orderId,
        final String orderCode,
        final OffsetDateTime orderedAt,
        final String orderStatus) {

        OrderJpaEntity order = new OrderJpaEntity();
        order.setId(orderId);
        order.setOrderCode(orderCode);
        order.setOrderedAt(orderedAt);
        order.setOrderStatus(orderStatus);
        order.setCreatedBy("system");
        order.setUpdatedBy("before-update");
        return order;
    }
}

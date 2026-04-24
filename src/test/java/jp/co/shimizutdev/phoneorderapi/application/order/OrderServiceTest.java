package jp.co.shimizutdev.phoneorderapi.application.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCannotBeCancelledException;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaEntity;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaRepository;
import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 注文サービステスト
 */
@SpringBootTest
class OrderServiceTest extends AbstractPostgreSQLTest {

    /**
     * 注文サービス
     */
    @Autowired
    private OrderService orderService;

    /**
     * 注文リポジトリ
     */
    @Autowired
    private OrderJpaRepository orderJpaRepository;

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
     * 注文コードで注文を取得できること。
     *
     * Given 注文データが保存されている
     * When 注文コードで注文を取得する
     * Then 対象注文が取得できる
     * </pre>
     */
    @Test
    @DisplayName("注文コードで注文を取得できること")
    void shouldGetOrderByOrderCode() {
        orderJpaRepository.save(reconstructOrderJpaEntity("ORD000001", "001"));

        Optional<Order> actual = orderService.getOrderByOrderCode("ORD000001");

        assertTrue(actual.isPresent());
        assertEquals("ORD000001", actual.get().getOrderCode().getValue());
        assertEquals("001", actual.get().getOrderStatus().getCode());
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
    void shouldListOrders() {
        orderJpaRepository.save(reconstructOrderJpaEntity("ORD000001", "001"));
        orderJpaRepository.save(reconstructOrderJpaEntity("ORD000002", "002"));

        List<Order> actual = orderService.getOrders();

        assertEquals(2, actual.size());
        assertTrue(actual.stream().anyMatch(order -> "ORD000001".equals(order.getOrderCode().getValue())));
        assertTrue(actual.stream().anyMatch(order -> "ORD000002".equals(order.getOrderCode().getValue())));
    }

    /**
     * <pre>
     * 注文を登録できること。
     *
     * Given 保存対象の注文レスポンスを用意する
     * When 注文を登録する
     * Then 注文が保存され注文コードと初期ステータスが設定される
     * </pre>
     */
    @Test
    @DisplayName("注文を登録できること")
    void shouldCreateOrder() {
        Order createdOrder = orderService.createOrder(OffsetDateTime.now());

        assertNotNull(createdOrder.getOrderId());
        assertTrue(createdOrder.getOrderCode().getValue().startsWith("ORD"));
        assertEquals("001", createdOrder.getOrderStatus().getCode());

        Optional<OrderJpaEntity> actual = orderJpaRepository.findByOrderCode(createdOrder.getOrderCode().getValue());
        assertTrue(actual.isPresent());
    }

    /**
     * <pre>
     * 注文をキャンセルできること。
     *
     * Given 注文データが保存されている
     * When 注文をキャンセルする
     * Then 注文ステータスがキャンセルで保存される
     * </pre>
     */
    @Test
    @DisplayName("注文をキャンセルできること")
    void shouldCancelOrder() {
        orderJpaRepository.save(reconstructOrderJpaEntity("ORD000001", "001"));

        Optional<Order> actual = orderService.cancelOrder("ORD000001");

        assertTrue(actual.isPresent());
        assertEquals("006", actual.get().getOrderStatus().getCode());

        Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findByOrderCode("ORD000001");
        assertTrue(savedOrder.isPresent());
        assertEquals("006", savedOrder.get().getOrderStatus());
    }

    /**
     * <pre>
     * 完了済み注文はキャンセルできないこと。
     *
     * Given 完了状態の注文データが保存されている
     * When 注文をキャンセルする
     * Then 注文キャンセル不可例外が発生する
     * </pre>
     */
    @Test
    @DisplayName("完了済み注文はキャンセルできないこと")
    void shouldThrowExceptionWhenCompletedOrderIsCancelled() {
        orderJpaRepository.save(reconstructOrderJpaEntity("ORD000001", "005"));

        assertThrows(
            OrderCannotBeCancelledException.class,
            () -> orderService.cancelOrder("ORD000001")
        );
    }

    /**
     * 注文JPAエンティティを再構築する
     *
     * @param orderCode   注文コード
     * @param orderStatus 注文ステータス
     * @return 注文JPAエンティティ
     */
    private OrderJpaEntity reconstructOrderJpaEntity(final String orderCode, final String orderStatus) {
        OrderJpaEntity order = new OrderJpaEntity();
        order.setId(UUID.randomUUID());
        order.setOrderCode(orderCode);
        order.setOrderedAt(OffsetDateTime.now());
        order.setOrderStatus(orderStatus);
        order.setCreatedBy("system");
        order.setUpdatedBy("system");
        return order;
    }
}

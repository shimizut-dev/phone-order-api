package jp.co.shimizutdev.phoneorderapi.application.order;

import jakarta.persistence.EntityManager;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 注文サービステスト
 */
@SpringBootTest
@Transactional
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
     * 注文データをDBへ直接登録する
     *
     * @param orderCode   注文コード
     * @param orderStatus 注文ステータス
     * @param version     バージョン
     */
    private void insertOrder(
        final String orderCode,
        final String orderStatus,
        final long version) {

        //noinspection SqlResolve,SqlNoDataSourceInspection
        entityManager.createNativeQuery("""
                insert into orders (id, order_code, ordered_at, order_status, version, created_by, updated_by)
                values (:id, :orderCode, :orderedAt, :orderStatus, :version, 'system', 'system')
                """)
            .setParameter("id", UUID.randomUUID())
            .setParameter("orderCode", orderCode)
            .setParameter("orderedAt", OffsetDateTime.now())
            .setParameter("orderStatus", orderStatus)
            .setParameter("version", version)
            .executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    @Nested
    @DisplayName("getOrderByOrderCode")
    class GetOrderByOrderCode {

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
            insertOrder("ORD000001", "001", 2L);

            Order actual = orderService.getOrderByOrderCode("ORD000001");

            assertEquals("ORD000001", actual.getOrderCode().getValue());
            assertEquals("001", actual.getOrderStatus().getCode());
            assertEquals(Version.of(2L), actual.getVersion());
        }

        /**
         * <pre>
         * 注文コードに対応する注文が存在しない場合に注文未存在例外が発生すること。
         *
         * Given 対象注文が登録されていない
         * When 注文コードで注文を取得する
         * Then 注文未存在例外が発生する
         * </pre>
         */
        @Test
        @DisplayName("注文コードに対応する注文が存在しない場合に注文未存在例外が発生すること")
        void shouldThrowExceptionWhenOrderDoesNotExist() {
            assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderByOrderCode("ORD999999")
            );
        }
    }

    @Nested
    @DisplayName("getOrders")
    class GetOrders {

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
        void shouldGetOrders() {
            insertOrder("ORD000001", "001", 0L);
            insertOrder("ORD000002", "002", 1L);

            List<Order> actual = orderService.getOrders();

            assertEquals(2, actual.size());
            assertTrue(actual.stream().anyMatch(order ->
                "ORD000001".equals(order.getOrderCode().getValue()) && Version.of(0L).equals(order.getVersion())));
            assertTrue(actual.stream().anyMatch(order ->
                "ORD000002".equals(order.getOrderCode().getValue()) && Version.of(1L).equals(order.getVersion())));
        }
    }

    @Nested
    @DisplayName("createOrder")
    class CreateOrder {

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
            assertEquals(Version.of(0L), createdOrder.getVersion());

            Optional<OrderJpaEntity> actual = orderJpaRepository.findByOrderCode(createdOrder.getOrderCode().getValue());
            assertTrue(actual.isPresent());
            assertEquals(0L, actual.get().getVersion());
        }

        /**
         * <pre>
         * リクエストヘッダーのユーザー情報が監査項目へ反映されること。
         *
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
                Order createdOrder = orderService.createOrder(OffsetDateTime.now());

                Optional<OrderJpaEntity> actual = orderJpaRepository.findByOrderCode(createdOrder.getOrderCode().getValue());
                assertTrue(actual.isPresent());
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
            insertOrder("ORD000001", "001", 0L);

            Order actual = orderService.cancelOrder("ORD000001", 0L);

            assertEquals("006", actual.getOrderStatus().getCode());
            assertEquals(Version.of(1L), actual.getVersion());

            Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findByOrderCode("ORD000001");
            assertTrue(savedOrder.isPresent());
            assertEquals("006", savedOrder.get().getOrderStatus());
            assertEquals(1L, savedOrder.get().getVersion());
        }

        /**
         * <pre>
         * リクエストヘッダーのユーザー情報が更新監査項目へ反映されること。
         *
         * Given 既存注文と X-User-Id を含むリクエストコンテキストを用意する
         * When 注文をキャンセルする
         * Then updatedBy にリクエストユーザーが保存される
         * </pre>
         */
        @Test
        @DisplayName("リクエストヘッダーのユーザー情報が更新監査項目へ反映されること")
        void shouldUseRequestUserForAuditWhenCancellingOrder() {
            insertOrder("ORD000001", "001", 0L);
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("X-User-Id", "operator-1");
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            try {
                orderService.cancelOrder("ORD000001", 0L);

                Optional<OrderJpaEntity> savedOrder = orderJpaRepository.findByOrderCode("ORD000001");
                assertTrue(savedOrder.isPresent());
                assertEquals("operator-1", savedOrder.get().getUpdatedBy());
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }

        @Test
        @DisplayName("未存在の注文キャンセルは注文未存在例外になる")
        void shouldThrowExceptionWhenCancelTargetDoesNotExist() {
            assertThrows(
                OrderNotFoundException.class,
                () -> orderService.cancelOrder("ORD999999", 0L)
            );
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
            insertOrder("ORD000001", "005", 0L);

            assertThrows(
                OrderCannotBeCancelledException.class,
                () -> orderService.cancelOrder("ORD000001", 0L)
            );
        }

        /**
         * <pre>
         * stale version でキャンセルした場合は競合例外が発生すること。
         *
         * Given 対象注文データが保存されている
         * When 古いversionではなく不一致のversionで注文をキャンセルする
         * Then 注文楽観的ロック競合例外が発生する
         * </pre>
         */
        @Test
        @DisplayName("stale version でキャンセルすると競合になる")
        void shouldThrowExceptionWhenVersionDoesNotMatch() {
            insertOrder("ORD000001", "001", 0L);

            assertThrows(
                OrderVersionConflictException.class,
                () -> orderService.cancelOrder("ORD000001", 1L)
            );
        }
    }
}

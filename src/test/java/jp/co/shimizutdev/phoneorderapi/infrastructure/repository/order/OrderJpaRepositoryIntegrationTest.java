package jp.co.shimizutdev.phoneorderapi.infrastructure.repository.order;

import jp.co.shimizutdev.phoneorderapi.infrastructure.config.JpaAuditConfig;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaEntity;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaRepository;
import jp.co.shimizutdev.phoneorderapi.support.AbstractPostgreSQLIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 注文リポジトリ統合テスト
 */
@DataJpaTest
@Import(JpaAuditConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderJpaRepositoryIntegrationTest extends AbstractPostgreSQLIntegrationTest {

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
     * When 注文コードで検索する
     * Then 対象注文が1件取得できる
     * </pre>
     */
    @Test
    @DisplayName("注文コードで注文を取得できること")
    void shouldFindOrderByOrderCode() {
        orderJpaRepository.save(reconstructOrder(UUID.randomUUID()));

        Optional<OrderJpaEntity> actual = orderJpaRepository.findByOrderCode("ORD000001");

        assertTrue(actual.isPresent());
        assertEquals("ORD000001", actual.get().getOrderCode());
        assertEquals("001", actual.get().getOrderStatus());
    }

    /**
     * <pre>
     * 存在しない注文コードの場合は空を返すこと。
     *
     * Given 注文データが保存されていない
     * When 注文コードで検索する
     * Then 空が返る
     * </pre>
     */
    @Test
    @DisplayName("存在しない注文コードの場合は空を返すこと")
    void shouldReturnEmptyWhenOrderCodeDoesNotExist() {
        Optional<OrderJpaEntity> actual = orderJpaRepository.findByOrderCode("ORD999999");

        assertTrue(actual.isEmpty());
    }

    /**
     * 注文を作成する
     *
     * @param orderId 注文ID
     * @return 注文
     */
    private OrderJpaEntity reconstructOrder(final UUID orderId) {
        OrderJpaEntity order = new OrderJpaEntity();
        order.setId(orderId);
        order.setOrderCode("ORD000001");
        order.setOrderedAt(OffsetDateTime.now());
        order.setOrderStatus("001");
        order.setCreatedBy("system");
        order.setUpdatedBy("system");

        return order;
    }
}

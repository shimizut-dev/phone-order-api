package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * 注文JPAリポジトリ
 */
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    /**
     * 注文コードで注文JPAエンティティを取得
     *
     * @param orderCode 注文コード
     * @return 注文JPAエンティティ
     */
    Optional<OrderJpaEntity> findByOrderCode(String orderCode);

    /**
     * DB の注文コード採番関数を呼び出す。
     *
     * @return 採番済み注文コード
     */
    @Query(value = "select next_order_code()", nativeQuery = true)
    String nextOrderCode();
}

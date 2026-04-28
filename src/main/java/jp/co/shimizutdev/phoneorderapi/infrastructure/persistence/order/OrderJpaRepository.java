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
     * @return 注文JPAエンティティ。存在しない場合は空の Optional
     */
    Optional<OrderJpaEntity> findByOrderCode(String orderCode);

    /**
     * 注文IDとバージョンで注文JPAエンティティを取得する
     *
     * @param id      注文ID
     * @param version バージョン
     * @return 注文JPAエンティティ。存在しない場合は空の Optional
     */
    Optional<OrderJpaEntity> findByIdAndVersion(UUID id, Long version);

    /**
     * DB の注文コード採番関数を呼び出す
     *
     * @return 採番済み注文コード
     */
    @Query(value = "select next_order_code()", nativeQuery = true)
    String nextOrderCode();
}

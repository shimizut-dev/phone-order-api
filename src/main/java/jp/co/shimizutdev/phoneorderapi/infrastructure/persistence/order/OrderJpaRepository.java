package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 注文JPAリポジトリ
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    /**
     * 注文コードで注文JPAエンティティを取得
     *
     * @param orderCode 注文コード
     * @return 注文JPAエンティティ
     */
    Optional<OrderJpaEntity> findByOrderCode(String orderCode);

    /**
     * 指定プレフィックスで始まる注文JPAエンティティの最大値を取得
     *
     * @param prefix 注文コードプレフィックス
     * @return 注文JPAエンティティ
     */
    Optional<OrderJpaEntity> findTopByOrderCodeStartingWithOrderByOrderCodeDesc(String prefix);
}

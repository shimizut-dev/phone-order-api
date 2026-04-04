package jp.co.shimizutdev.phoneorderapi.repository;

import jp.co.shimizutdev.phoneorderapi.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 注文Repository
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    /**
     * 注文コードで注文を取得
     *
     * @param orderCode 注文コード
     * @return 注文
     */
    Optional<OrderEntity> findByOrderCode(String orderCode);
}

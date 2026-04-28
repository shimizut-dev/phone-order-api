package jp.co.shimizutdev.phoneorderapi.infrastructure.repository.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCode;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderRepository;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderVersionConflictException;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaEntity;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaMapper;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 注文リポジトリ実装
 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    /**
     * 注文JPAリポジトリ
     */
    private final OrderJpaRepository orderJpaRepository;

    /**
     * エンティティマネージャ
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 注文一覧を取得する
     *
     * @return 注文一覧
     */
    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
            .map(OrderJpaMapper::toDomain)
            .toList();
    }

    /**
     * 注文コードで注文を取得する
     *
     * @param orderCode 注文コード
     * @return 注文
     */
    @Override
    public Optional<Order> findByOrderCode(final OrderCode orderCode) {
        return orderJpaRepository.findByOrderCode(orderCode.getValue())
            .map(OrderJpaMapper::toDomain);
    }

    /**
     * 注文を登録する
     *
     * @param order 注文
     * @return 注文
     */
    @Override
    public Order create(final Order order) {
        OrderJpaEntity orderJpaEntity = OrderJpaMapper.toNewEntity(order);
        entityManager.persist(orderJpaEntity);
        entityManager.flush();
        return OrderJpaMapper.toDomain(orderJpaEntity);
    }

    /**
     * 注文を更新する
     *
     * @param order 注文
     * @return 注文
     */
    @Override
    public Order update(final Order order) {
        OrderJpaEntity orderJpaEntity = orderJpaRepository.findByIdAndVersion(
                order.getOrderId().getValue(),
                order.getVersion().getValue())
            .orElseThrow(OrderVersionConflictException::new);

        orderJpaEntity.setOrderStatus(order.getOrderStatus().getCode());
        entityManager.flush();

        return OrderJpaMapper.toDomain(orderJpaEntity);
    }
}

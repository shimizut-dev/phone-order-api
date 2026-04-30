package jp.co.shimizutdev.phoneorderapi.infrastructure.repository.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import jp.co.shimizutdev.phoneorderapi.domain.common.PageResult;
import jp.co.shimizutdev.phoneorderapi.domain.common.PagingCondition;
import jp.co.shimizutdev.phoneorderapi.domain.order.Order;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderCode;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderRepository;
import jp.co.shimizutdev.phoneorderapi.domain.order.OrderVersionConflictException;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.InvalidPersistedOrderException;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaEntity;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaMapper;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/** 注文リポジトリ実装 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

  /** 注文JPAリポジトリ */
  private final OrderJpaRepository orderJpaRepository;

  /** エンティティマネージャ */
  @PersistenceContext private EntityManager entityManager;

  /**
   * 注文一覧を取得する
   *
   * @param pagingCondition ページング条件
   * @return 注文日時降順、同一日時は注文コード降順のページング済み注文一覧。存在しない場合または範囲外ページの場合は空のページ
   * @throws InvalidPersistedOrderException 永続化済み注文データが不正な場合
   */
  @Override
  public PageResult<Order> findAll(final PagingCondition pagingCondition) {
    Page<OrderJpaEntity> page =
        orderJpaRepository.findAll(
            PageRequest.of(
                pagingCondition.page(),
                pagingCondition.size(),
                Sort.by(Sort.Direction.DESC, "orderedAt")
                    .and(Sort.by(Sort.Direction.DESC, "orderCode"))));

    List<Order> orders = page.getContent().stream().map(OrderJpaMapper::toDomain).toList();

    return new PageResult<>(
        orders,
        pagingCondition.page(),
        pagingCondition.size(),
        page.getTotalElements(),
        page.getTotalPages());
  }

  /**
   * 注文コードで注文を取得する
   *
   * @param orderCode 注文コード
   * @return 注文。存在しない場合は空の Optional
   * @throws InvalidPersistedOrderException 永続化済み注文データが不正な場合
   */
  @Override
  public Optional<Order> findByOrderCode(final OrderCode orderCode) {
    return orderJpaRepository.findByOrderCode(orderCode.getValue()).map(OrderJpaMapper::toDomain);
  }

  /**
   * 注文を登録する
   *
   * @param order 注文
   * @return 注文
   * @throws InvalidPersistedOrderException 登録後の永続化済み注文データが不正な場合
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
   * @throws OrderVersionConflictException 更新対象の注文バージョンが一致しない場合
   * @throws InvalidPersistedOrderException 更新後の永続化済み注文データが不正な場合
   */
  @Override
  public Order update(final Order order) {
    OrderJpaEntity orderJpaEntity =
        orderJpaRepository
            .findByIdAndVersion(order.getOrderId().getValue(), order.getVersion().getValue())
            .orElseThrow(() -> OrderVersionConflictException.byOrderForUpdate(order));

    orderJpaEntity.setOrderStatus(order.getOrderStatus().getCode());
    entityManager.flush();

    return OrderJpaMapper.toDomain(orderJpaEntity);
  }
}

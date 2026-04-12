package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.*;

/**
 * 注文JPAマッパー
 */
public class OrderJpaMapper {
    /**
     * システムユーザ
     */
    private static final String SYSTEM_USER = "system";

    /**
     * コンストラクタ(インスタンス化を防止)
     */
    private OrderJpaMapper() {
    }

    /**
     * 注文JPAエンティティを注文へ変換する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文
     */
    public static Order toDomain(final OrderJpaEntity orderJpaEntity) {
        return Order.reconstruct(
            OrderId.of(orderJpaEntity.getId()),
            OrderCode.of(orderJpaEntity.getOrderCode()),
            OrderedAt.of(orderJpaEntity.getOrderedAt()),
            OrderStatus.toOrderStatus(orderJpaEntity.getOrderStatus())
        );
    }

    /**
     * 注文を注文JPAエンティティへ変換する
     *
     * @param order 注文
     * @return 注文JPAエンティティ
     */
    public static OrderJpaEntity toEntity(final Order order) {
        OrderJpaEntity orderJpaEntity = new OrderJpaEntity();
        orderJpaEntity.setId(order.getOrderId().getValue());
        orderJpaEntity.setOrderCode(order.getOrderCode().getValue());
        orderJpaEntity.setOrderedAt(order.getOrderedAt().getValue());
        orderJpaEntity.setOrderStatus(order.getOrderStatus().getCode());
        orderJpaEntity.setCreatedBy(SYSTEM_USER);
        orderJpaEntity.setUpdatedBy(SYSTEM_USER);
        return orderJpaEntity;
    }
}

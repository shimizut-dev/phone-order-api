package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.*;

/**
 * 注文JPAマッパー
 */
public class OrderJpaMapper {

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
            toOrderId(orderJpaEntity),
            toOrderCode(orderJpaEntity),
            toOrderedAt(orderJpaEntity),
            toOrderStatus(orderJpaEntity)
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
        return orderJpaEntity;
    }

    /**
     * 注文IDを再構築する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文ID
     */
    private static OrderId toOrderId(final OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity.getId() == null) {
            throw new InvalidPersistedOrderException(InvalidPersistedOrderMessages.INVALID_ORDER_ID);
        }

        return OrderId.of(orderJpaEntity.getId());
    }

    /**
     * 注文コードを再構築する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文コード
     */
    private static OrderCode toOrderCode(final OrderJpaEntity orderJpaEntity) {
        if (!OrderCode.isValid(orderJpaEntity.getOrderCode())) {
            throw new InvalidPersistedOrderException(InvalidPersistedOrderMessages.INVALID_ORDER_CODE);
        }

        return OrderCode.of(orderJpaEntity.getOrderCode());
    }

    /**
     * 注文日時を再構築する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文日時
     */
    private static OrderedAt toOrderedAt(final OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity.getOrderedAt() == null) {
            throw new InvalidPersistedOrderException(InvalidPersistedOrderMessages.INVALID_ORDERED_AT);
        }

        return OrderedAt.of(orderJpaEntity.getOrderedAt());
    }

    /**
     * 注文ステータスを再構築する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文ステータス
     */
    private static OrderStatus toOrderStatus(final OrderJpaEntity orderJpaEntity) {
        if (!OrderStatus.isValidCode(orderJpaEntity.getOrderStatus())) {
            throw new InvalidPersistedOrderException(InvalidPersistedOrderMessages.INVALID_ORDER_STATUS);
        }

        return OrderStatus.fromCode(orderJpaEntity.getOrderStatus());
    }
}

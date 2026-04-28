package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

import jp.co.shimizutdev.phoneorderapi.domain.order.*;

/**
 * 注文JPAマッパー
 */
public class OrderJpaMapper {

    /**
     * 永続化済み注文データの注文ID不正時の例外メッセージ。
     */
    private static final String INVALID_ORDER_ID_MESSAGE = "永続化済み注文データの注文IDが不正です。";

    /**
     * 永続化済み注文データの注文コード不正時の例外メッセージ。
     */
    private static final String INVALID_ORDER_CODE_MESSAGE = "永続化済み注文データの注文コードが不正です。";

    /**
     * 永続化済み注文データの注文日時不正時の例外メッセージ。
     */
    private static final String INVALID_ORDERED_AT_MESSAGE = "永続化済み注文データの注文日時が不正です。";

    /**
     * 永続化済み注文データの注文ステータス不正時の例外メッセージ。
     */
    private static final String INVALID_ORDER_STATUS_MESSAGE = "永続化済み注文データの注文ステータスが不正です。";

    /**
     * 永続化済み注文データのバージョン不正時の例外メッセージ。
     */
    private static final String INVALID_ORDER_VERSION_MESSAGE = "永続化済み注文データのバージョンが不正です。";

    /**
     * コンストラクタ(インスタンス化を禁止)
     */
    private OrderJpaMapper() {
    }

    /**
     * 注文JPAエンティティを注文へ変換する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文
     * @throws InvalidPersistedOrderException 永続化済み注文データが不正な場合
     */
    public static Order toDomain(final OrderJpaEntity orderJpaEntity) {
        return Order.reconstruct(
            toOrderId(orderJpaEntity),
            toOrderCode(orderJpaEntity),
            toOrderedAt(orderJpaEntity),
            toOrderStatus(orderJpaEntity),
            toVersion(orderJpaEntity)
        );
    }

    /**
     * 新規登録用の注文を注文JPAエンティティへ変換する。
     * `@Version` の初期値は JPA に採番させるため設定しない。
     *
     * @param order 注文
     * @return 注文JPAエンティティ
     */
    public static OrderJpaEntity toNewEntity(final Order order) {
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
     * @throws InvalidPersistedOrderException 永続化済み注文データの注文IDが不正な場合
     */
    private static OrderId toOrderId(final OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity.getId() == null) {
            throw InvalidPersistedOrderException.byOrderJpaEntity(
                INVALID_ORDER_ID_MESSAGE,
                orderJpaEntity
            );
        }

        return OrderId.of(orderJpaEntity.getId());
    }

    /**
     * 注文コードを再構築する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文コード
     * @throws InvalidPersistedOrderException 永続化済み注文データの注文コードが不正な場合
     */
    private static OrderCode toOrderCode(final OrderJpaEntity orderJpaEntity) {
        if (!OrderCode.isValid(orderJpaEntity.getOrderCode())) {
            throw InvalidPersistedOrderException.byOrderJpaEntity(
                INVALID_ORDER_CODE_MESSAGE,
                orderJpaEntity
            );
        }

        return OrderCode.of(orderJpaEntity.getOrderCode());
    }

    /**
     * 注文日時を再構築する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文日時
     * @throws InvalidPersistedOrderException 永続化済み注文データの注文日時が不正な場合
     */
    private static OrderedAt toOrderedAt(final OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity.getOrderedAt() == null) {
            throw InvalidPersistedOrderException.byOrderJpaEntity(
                INVALID_ORDERED_AT_MESSAGE,
                orderJpaEntity
            );
        }

        return OrderedAt.of(orderJpaEntity.getOrderedAt());
    }

    /**
     * 注文ステータスを再構築する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return 注文ステータス
     * @throws InvalidPersistedOrderException 永続化済み注文データの注文ステータスが不正な場合
     */
    private static OrderStatus toOrderStatus(final OrderJpaEntity orderJpaEntity) {
        if (!OrderStatus.isValidCode(orderJpaEntity.getOrderStatus())) {
            throw InvalidPersistedOrderException.byOrderJpaEntity(
                INVALID_ORDER_STATUS_MESSAGE,
                orderJpaEntity
            );
        }

        return OrderStatus.fromCode(orderJpaEntity.getOrderStatus());
    }

    /**
     * バージョンを再構築する
     *
     * @param orderJpaEntity 注文JPAエンティティ
     * @return バージョン
     * @throws InvalidPersistedOrderException 永続化済み注文データのバージョンが不正な場合
     */
    private static Version toVersion(final OrderJpaEntity orderJpaEntity) {
        if (orderJpaEntity.getVersion() == null || orderJpaEntity.getVersion() < 0) {
            throw InvalidPersistedOrderException.byOrderJpaEntity(
                INVALID_ORDER_VERSION_MESSAGE,
                orderJpaEntity
            );
        }

        return Version.of(orderJpaEntity.getVersion());
    }
}

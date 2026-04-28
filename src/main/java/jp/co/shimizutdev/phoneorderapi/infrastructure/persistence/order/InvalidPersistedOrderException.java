package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

/**
 * 永続化済み注文データがドメイン再構築できない場合の例外
 */
public class InvalidPersistedOrderException extends RuntimeException {

    /**
     * 永続化済み注文データ不整合時のログ用メッセージテンプレート。
     */
    private static final String MESSAGE_TEMPLATE = "%s: orderId=%s, orderCode=%s";

    /**
     * コンストラクタ
     *
     * @param message メッセージ
     */
    private InvalidPersistedOrderException(final String message) {
        super(message);
    }

    /**
     * 永続化済み注文エンティティの識別情報を含む例外を生成する。
     *
     * @param message        不整合内容を表すメッセージ
     * @param orderJpaEntity 不整合が検出された注文JPAエンティティ
     * @return 永続化済み注文データ不整合例外
     */
    public static InvalidPersistedOrderException byOrderJpaEntity(
        final String message,
        final OrderJpaEntity orderJpaEntity) {

        return new InvalidPersistedOrderException(
            MESSAGE_TEMPLATE.formatted(
                message,
                orderJpaEntity.getId(),
                orderJpaEntity.getOrderCode()
            )
        );
    }
}

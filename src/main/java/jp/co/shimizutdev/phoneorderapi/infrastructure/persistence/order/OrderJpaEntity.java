package jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jp.co.shimizutdev.phoneorderapi.infrastructure.persistence.BaseAuditJpaEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 注文JPAエンティティ
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderJpaEntity extends BaseAuditJpaEntity {

    /**
     * 注文ID
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /**
     * 注文コード
     */
    @Column(name = "order_code", nullable = false, length = 20)
    private String orderCode;

    /**
     * 注文日時
     */
    @Column(name = "ordered_at", nullable = false)
    private OffsetDateTime orderedAt;

    /**
     * 注文ステータス
     */
    @Column(name = "order_status", nullable = false, length = 10)
    private String orderStatus;
}

package jp.co.shimizutdev.phoneorderapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 注文
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseAuditEntity {

    /**
     * 注文ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

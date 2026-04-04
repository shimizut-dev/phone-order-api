package jp.co.shimizutdev.phoneorderapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * 配送履歴
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "delivery_histories")
public class DeliveryHistoryEntity extends BaseAuditEntity {

    /**
     * 配送履歴ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /**
     * 配送
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private DeliveryEntity delivery;

    /**
     * 配送ステータス
     */
    @Column(name = "delivery_status", nullable = false, length = 10)
    private String deliveryStatus;
}

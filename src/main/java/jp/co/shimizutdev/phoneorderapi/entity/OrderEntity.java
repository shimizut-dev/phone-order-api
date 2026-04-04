package jp.co.shimizutdev.phoneorderapi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * 注文明細List
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineEntity> orderLines = new ArrayList<>();

    /**
     * 注文関係者List
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderPartyEntity> orderParties = new ArrayList<>();

    /**
     * 配送List
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryEntity> deliveries = new ArrayList<>();

    /**
     * 注文履歴List
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderHistoryEntity> orderHistories = new ArrayList<>();
}

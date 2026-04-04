package jp.co.shimizutdev.phoneorderapi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * 注文明細
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_lines")
public class OrderLineEntity extends BaseAuditEntity {

    /**
     * 注文明細ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /**
     * 注文
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    /**
     * 注文明細コード
     */
    @Column(name = "order_line_code", nullable = false, length = 20)
    private String orderLineCode;

    /**
     * 注文明細区分
     */
    @Column(name = "order_line_division", nullable = false, length = 10)
    private String orderLineDivision;

    /**
     * 注文数量
     */
    @Column(name = "order_quantity", nullable = false)
    private Integer orderQuantity;

    /**
     * 回線
     */
    @OneToOne(mappedBy = "orderLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private LineEntity line;

    /**
     * 移動機
     */
    @OneToOne(mappedBy = "orderLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private PhoneEntity phone;

    /**
     * アクセサリ
     */
    @OneToOne(mappedBy = "orderLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private AccessoryEntity accessory;
}

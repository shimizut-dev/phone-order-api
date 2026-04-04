package jp.co.shimizutdev.phoneorderapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * 移動機
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phones")
public class PhoneEntity extends BaseAuditEntity {

    /**
     * 移動機ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /**
     * 注文明細
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_line_id", nullable = false)
    private OrderLineEntity orderLine;

    /**
     * 移動機コード
     */
    @Column(name = "phone_code", nullable = false, length = 20)
    private String phoneCode;

    /**
     * IMEI
     */
    @Column(name = "imei", nullable = false, length = 15)
    private String imei;
}

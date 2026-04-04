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
 * 回線
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lines")
public class LineEntity extends BaseAuditEntity {

    /**
     * 回線ID
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
     * 回線コード
     */
    @Column(name = "line_code", nullable = false, length = 20)
    private String lineCode;

    /**
     * 回線契約区分
     */
    @Column(name = "line_contract_division", nullable = false, length = 10)
    private String lineContractDivision;

    /**
     * MSISDN
     */
    @Column(name = "msisdn", nullable = false, length = 15)
    private String msisdn;
}

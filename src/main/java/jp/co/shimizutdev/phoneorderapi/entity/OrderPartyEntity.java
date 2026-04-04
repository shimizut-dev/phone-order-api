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

import java.time.LocalDate;
import java.util.UUID;

/**
 * 注文関係者
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_parties")
public class OrderPartyEntity extends BaseAuditEntity {

    /**
     * 注文関係者ID
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
     * 注文関係者コード
     */
    @Column(name = "order_party_code", nullable = false, length = 20)
    private String orderPartyCode;

    /**
     * 注文関係者役割区分
     */
    @Column(name = "order_party_role_division", nullable = false, length = 10)
    private String orderPartyRoleDivision;

    /**
     * 注文関係者姓
     */
    @Column(name = "order_party_last_name", nullable = false, length = 50)
    private String orderPartyLastName;

    /**
     * 注文関係者名
     */
    @Column(name = "order_party_first_name", nullable = false, length = 50)
    private String orderPartyFirstName;

    /**
     * 注文関係者姓カナ
     */
    @Column(name = "order_party_last_name_kana", nullable = false, length = 50)
    private String orderPartyLastNameKana;

    /**
     * 注文関係者名カナ
     */
    @Column(name = "order_party_first_name_kana", nullable = false, length = 50)
    private String orderPartyFirstNameKana;

    /**
     * 注文関係者性別区分
     */
    @Column(name = "order_party_gender_division", nullable = false, length = 10)
    private String orderPartyGenderDivision;

    /**
     * 注文関係者生年月日
     */
    @Column(name = "order_party_birth_date", nullable = false)
    private LocalDate orderPartyBirthDate;

    /**
     * 注文関係者郵便番号
     */
    @Column(name = "order_party_postal_code", nullable = false, length = 7)
    private String orderPartyPostalCode;

    /**
     * 注文関係者都道府県
     */
    @Column(name = "order_party_prefecture", nullable = false, length = 20)
    private String orderPartyPrefecture;

    /**
     * 注文関係者市区町村
     */
    @Column(name = "order_party_city", nullable = false, length = 100)
    private String orderPartyCity;

    /**
     * 注文関係者町域
     */
    @Column(name = "order_party_town", length = 100)
    private String orderPartyTown;

    /**
     * 注文関係者番地
     */
    @Column(name = "order_party_street_address", nullable = false, length = 100)
    private String orderPartyStreetAddress;

    /**
     * 注文関係者建物名
     */
    @Column(name = "order_party_building_name", nullable = false, length = 100)
    private String orderPartyBuildingName;
}

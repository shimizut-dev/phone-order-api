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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 配送
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "deliveries")
public class DeliveryEntity extends BaseAuditEntity {

    /**
     * 配送ID
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
     * 配送コード
     */
    @Column(name = "delivery_code", nullable = false, length = 20)
    private String deliveryCode;

    /**
     * 配送ステータス
     */
    @Column(name = "delivery_status", nullable = false, length = 10)
    private String deliveryStatus;

    /**
     * 受取人姓
     */
    @Column(name = "recipient_last_name", nullable = false, length = 50)
    private String recipientLastName;

    /**
     * 受取人名
     */
    @Column(name = "recipient_first_name", nullable = false, length = 50)
    private String recipientFirstName;

    /**
     * 受取人姓カナ
     */
    @Column(name = "recipient_last_name_kana", nullable = false, length = 50)
    private String recipientLastNameKana;

    /**
     * 受取人名カナ
     */
    @Column(name = "recipient_first_name_kana", length = 50)
    private String recipientFirstNameKana;

    /**
     * 配送郵便番号
     */
    @Column(name = "delivery_postal_code", nullable = false, length = 7)
    private String deliveryPostalCode;

    /**
     * 配送都道府県
     */
    @Column(name = "delivery_prefecture", nullable = false, length = 20)
    private String deliveryPrefecture;

    /**
     * 配送市区町村
     */
    @Column(name = "delivery_city", nullable = false, length = 100)
    private String deliveryCity;

    /**
     * 配送町域
     */
    @Column(name = "delivery_town", nullable = false, length = 100)
    private String deliveryTown;

    /**
     * 配送番地
     */
    @Column(name = "delivery_street_address", nullable = false, length = 100)
    private String deliveryStreetAddress;

    /**
     * 配送建物名
     */
    @Column(name = "delivery_building_name", length = 100)
    private String deliveryBuildingName;

    /**
     * 配送時間帯区分
     */
    @Column(name = "delivery_time_slot_division", nullable = false, length = 10)
    private String deliveryTimeSlotDivision;

    /**
     * 置き配区分
     */
    @Column(name = "leave_place_division", nullable = false, length = 10)
    private String leavePlaceDivision;

    /**
     * 置き場所区分
     */
    @Column(name = "leave_at_place_division", length = 10)
    private String leaveAtPlaceDivision;

    /**
     * 配送明細List
     */
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryLineEntity> deliveryLines = new ArrayList<>();

    /**
     * 配送履歴List
     */
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryHistoryEntity> deliveryHistories = new ArrayList<>();
}

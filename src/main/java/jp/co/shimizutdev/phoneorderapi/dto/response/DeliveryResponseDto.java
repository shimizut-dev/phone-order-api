package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 配送Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class DeliveryResponseDto extends BaseAuditResponseDto {

    /**
     * 配送ID
     */
    private UUID id;

    /**
     * 注文ID
     */
    private UUID orderId;

    /**
     * 配送コード
     */
    private String deliveryCode;

    /**
     * 配送ステータス
     */
    private String deliveryStatus;

    /**
     * 受取人姓
     */
    private String recipientLastName;

    /**
     * 受取人名
     */
    private String recipientFirstName;

    /**
     * 受取人姓カナ
     */
    private String recipientLastNameKana;

    /**
     * 受取人名カナ
     */
    private String recipientFirstNameKana;

    /**
     * 配送郵便番号
     */
    private String deliveryPostalCode;

    /**
     * 配送都道府県
     */
    private String deliveryPrefecture;

    /**
     * 配送市区町村
     */
    private String deliveryCity;

    /**
     * 配送町域
     */
    private String deliveryTown;

    /**
     * 配送番地
     */
    private String deliveryStreetAddress;

    /**
     * 配送建物名
     */
    private String deliveryBuildingName;

    /**
     * 配送時間帯区分
     */
    private String deliveryTimeSlotDivision;

    /**
     * 置き配区分
     */
    private String leavePlaceDivision;

    /**
     * 置き場所区分
     */
    private String leaveAtPlaceDivision;

    /**
     * 配送明細List
     */
    private List<DeliveryLineResponseDto> deliveryLines;

    /**
     * 配送履歴List
     */
    private List<DeliveryHistoryResponseDto> deliveryHistories;
}
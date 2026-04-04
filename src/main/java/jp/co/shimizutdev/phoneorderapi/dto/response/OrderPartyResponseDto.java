package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注文関係者Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderPartyResponseDto extends BaseAuditResponseDto {

    /**
     * 注文関係者ID
     */
    private UUID id;

    /**
     * 注文ID
     */
    private UUID orderId;

    /**
     * 注文関係者コード
     */
    private String orderPartyCode;

    /**
     * 注文関係者役割区分
     */
    private String orderPartyRoleDivision;

    /**
     * 注文関係者姓
     */
    private String orderPartyLastName;

    /**
     * 注文関係者名
     */
    private String orderPartyFirstName;

    /**
     * 注文関係者姓カナ
     */
    private String orderPartyLastNameKana;

    /**
     * 注文関係者名カナ
     */
    private String orderPartyFirstNameKana;

    /**
     * 注文関係者性別区分
     */
    private String orderPartyGenderDivision;

    /**
     * 注文関係者生年月日
     */
    private LocalDate orderPartyBirthDate;

    /**
     * 注文関係者郵便番号
     */
    private String orderPartyPostalCode;

    /**
     * 注文関係者都道府県
     */
    private String orderPartyPrefecture;

    /**
     * 注文関係者市区町村
     */
    private String orderPartyCity;

    /**
     * 注文関係者町域
     */
    private String orderPartyTown;

    /**
     * 注文関係者番地
     */
    private String orderPartyStreetAddress;

    /**
     * 注文関係者建物名
     */
    private String orderPartyBuildingName;
}
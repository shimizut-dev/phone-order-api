package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 配送Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class DeliveryRequestDto {

    /**
     * 配送コード
     */
    @NotBlank(message = "配送コードは必須です。")
    @Size(max = 20, message = "配送コードは20桁以内で入力してください。")
    private String deliveryCode;

    /**
     * 配送ステータス
     */
    @NotBlank(message = "配送ステータスは必須です。")
    @Size(max = 10, message = "配送ステータスは10桁以内で入力してください。")
    private String deliveryStatus;

    /**
     * 受取人姓
     */
    @NotBlank(message = "受取人姓は必須です。")
    @Size(max = 50, message = "受取人姓は50桁以内で入力してください。")
    private String recipientLastName;

    /**
     * 受取人名
     */
    @NotBlank(message = "受取人名は必須です。")
    @Size(max = 50, message = "受取人名は50桁以内で入力してください。")
    private String recipientFirstName;

    /**
     * 受取人姓カナ
     */
    @NotBlank(message = "受取人姓カナは必須です。")
    @Size(max = 50, message = "受取人姓カナは50桁以内で入力してください。")
    private String recipientLastNameKana;

    /**
     * 受取人名カナ
     */
    @Size(max = 50, message = "受取人名カナは50桁以内で入力してください。")
    private String recipientFirstNameKana;

    /**
     * 配送郵便番号
     */
    @NotBlank(message = "配送郵便番号は必須です。")
    @Pattern(regexp = "\\d{7}", message = "配送郵便番号は7桁の数字で入力してください。")
    private String deliveryPostalCode;

    /**
     * 配送都道府県
     */
    @NotBlank(message = "配送都道府県は必須です。")
    @Size(max = 20, message = "配送都道府県は20桁以内で入力してください。")
    private String deliveryPrefecture;

    /**
     * 配送市区町村
     */
    @NotBlank(message = "配送市区町村は必須です。")
    @Size(max = 100, message = "配送市区町村は100桁以内で入力してください。")
    private String deliveryCity;

    /**
     * 配送町域
     */
    @NotBlank(message = "配送町域は必須です。")
    @Size(max = 100, message = "配送町域は100桁以内で入力してください。")
    private String deliveryTown;

    /**
     * 配送番地
     */
    @NotBlank(message = "配送番地は必須です。")
    @Size(max = 100, message = "配送番地は100桁以内で入力してください。")
    private String deliveryStreetAddress;

    /**
     * 配送建物名
     */
    @Size(max = 100, message = "配送建物名は100桁以内で入力してください。")
    private String deliveryBuildingName;

    /**
     * 配送時間帯区分
     */
    @NotBlank(message = "配送時間帯区分は必須です。")
    @Size(max = 10, message = "配送時間帯区分は10桁以内で入力してください。")
    private String deliveryTimeSlotDivision;

    /**
     * 置き配区分
     */
    @NotBlank(message = "置き配区分は必須です。")
    @Size(max = 10, message = "置き配区分は10桁以内で入力してください。")
    private String leavePlaceDivision;

    /**
     * 置き場所区分
     */
    @Size(max = 10, message = "置き場所区分は10桁以内で入力してください。")
    private String leaveAtPlaceDivision;

    /**
     * 配送明細List
     */
    @Valid
    private List<DeliveryLineRequestDto> deliveryLines;

    /**
     * 配送履歴List
     */
    @Valid
    private List<DeliveryHistoryRequestDto> deliveryHistories;
}

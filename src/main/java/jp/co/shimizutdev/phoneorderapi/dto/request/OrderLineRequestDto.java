package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注文明細Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderLineRequestDto {

    /**
     * 注文明細コード
     */
    @NotBlank(message = "注文明細コードは必須です。")
    @Size(max = 20, message = "注文明細コードは20桁以内で入力してください。")
    private String orderLineCode;

    /**
     * 注文明細区分
     */
    @NotBlank(message = "注文明細区分は必須です。")
    @Size(max = 10, message = "注文明細区分は10桁以内で入力してください。")
    private String orderLineDivision;

    /**
     * 注文数量
     */
    @NotNull(message = "注文数量は必須です。")
    @Positive(message = "注文数量は1以上で入力してください。")
    private Integer orderQuantity;

    /**
     * 回線
     */
    @Valid
    private LineRequestDto line;

    /**
     * 移動機
     */
    @Valid
    private PhoneRequestDto phone;

    /**
     * アクセサリ
     */
    @Valid
    private AccessoryRequestDto accessory;
}

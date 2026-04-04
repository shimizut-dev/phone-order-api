package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * 配送明細Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class DeliveryLineRequestDto {

    /**
     * 配送明細コード
     */
    @NotBlank(message = "配送明細コードは必須です。")
    @Size(max = 20, message = "配送明細コードは20桁以内で入力してください。")
    private String deliveryLineCode;

    /**
     * 注文明細ID
     */
    @NotNull(message = "注文明細IDは必須です。")
    private UUID orderLineId;

    /**
     * 配送数量
     */
    @NotNull(message = "配送数量は必須です。")
    @Positive(message = "配送数量は1以上で入力してください。")
    private Integer deliveryQuantity;
}

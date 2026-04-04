package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 配送履歴Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class DeliveryHistoryRequestDto {

    /**
     * 配送ステータス
     */
    @NotBlank(message = "配送ステータスは必須です。")
    @Size(max = 10, message = "配送ステータスは10桁以内で入力してください。")
    private String deliveryStatus;
}

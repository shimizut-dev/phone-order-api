package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注文履歴Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderHistoryRequestDto {

    /**
     * 注文ステータス
     */
    @NotBlank(message = "注文ステータスは必須です。")
    @Size(max = 10, message = "注文ステータスは10桁以内で入力してください。")
    private String orderStatus;
}

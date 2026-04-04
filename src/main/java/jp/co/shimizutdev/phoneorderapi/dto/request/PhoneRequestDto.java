package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 移動機Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PhoneRequestDto {

    /**
     * 移動機コード
     */
    @NotBlank(message = "移動機コードは必須です。")
    @Size(max = 20, message = "移動機コードは20桁以内で入力してください。")
    private String phoneCode;

    /**
     * IMEI
     */
    @NotBlank(message = "IMEIは必須です。")
    @Pattern(regexp = "\\d{15}", message = "IMEIは15桁の数字で入力してください。")
    @Size(max = 15, message = "IMEIは15桁以内で入力してください。")
    private String imei;
}

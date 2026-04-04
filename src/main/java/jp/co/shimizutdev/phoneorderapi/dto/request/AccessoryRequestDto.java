package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * アクセサリRequest DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class AccessoryRequestDto {

    /**
     * アクセサリコード
     */
    @NotBlank(message = "アクセサリコードは必須です。")
    @Size(max = 20, message = "アクセサリコードは20桁以内で入力してください。")
    private String accessoryCode;
}

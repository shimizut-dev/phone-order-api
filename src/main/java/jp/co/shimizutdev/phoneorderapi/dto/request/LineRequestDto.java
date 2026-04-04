package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 回線Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class LineRequestDto {

    /**
     * 回線コード
     */
    @NotBlank(message = "回線コードは必須です。")
    @Size(max = 20, message = "回線コードは20桁以内で入力してください。")
    private String lineCode;

    /**
     * 回線契約区分
     */
    @NotBlank(message = "回線契約区分は必須です。")
    @Size(max = 10, message = "回線契約区分は10桁以内で入力してください。")
    private String lineContractDivision;

    /**
     * MSISDN
     */
    @NotBlank(message = "MSISDNは必須です。")
    @Pattern(regexp = "\\d{10,15}", message = "MSISDNは10桁から15桁の数字で入力してください。")
    @Size(max = 15, message = "MSISDNは15桁以内で入力してください。")
    private String msisdn;
}

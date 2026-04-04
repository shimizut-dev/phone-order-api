package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 注文関係者Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderPartyRequestDto {

    /**
     * 注文関係者コード
     */
    @NotBlank(message = "注文関係者コードは必須です。")
    @Size(max = 20, message = "注文関係者コードは20桁以内で入力してください。")
    private String orderPartyCode;

    /**
     * 注文関係者役割区分
     */
    @NotBlank(message = "注文関係者役割区分は必須です。")
    @Size(max = 10, message = "注文関係者役割区分は10桁以内で入力してください。")
    private String orderPartyRoleDivision;

    /**
     * 注文関係者姓
     */
    @NotBlank(message = "注文関係者姓は必須です。")
    @Size(max = 50, message = "注文関係者姓は50桁以内で入力してください。")
    private String orderPartyLastName;

    /**
     * 注文関係者名
     */
    @NotBlank(message = "注文関係者名は必須です。")
    @Size(max = 50, message = "注文関係者名は50桁以内で入力してください。")
    private String orderPartyFirstName;

    /**
     * 注文関係者姓カナ
     */
    @NotBlank(message = "注文関係者姓カナは必須です。")
    @Size(max = 50, message = "注文関係者姓カナは50桁以内で入力してください。")
    private String orderPartyLastNameKana;

    /**
     * 注文関係者名カナ
     */
    @NotBlank(message = "注文関係者名カナは必須です。")
    @Size(max = 50, message = "注文関係者名カナは50桁以内で入力してください。")
    private String orderPartyFirstNameKana;

    /**
     * 注文関係者性別区分
     */
    @NotBlank(message = "注文関係者性別区分は必須です。")
    @Size(max = 10, message = "注文関係者性別区分は10桁以内で入力してください。")
    private String orderPartyGenderDivision;

    /**
     * 注文関係者生年月日
     */
    @NotNull(message = "注文関係者生年月日は必須です。")
    @Past(message = "注文関係者生年月日は過去日で入力してください。")
    private LocalDate orderPartyBirthDate;

    /**
     * 注文関係者郵便番号
     */
    @NotBlank(message = "注文関係者郵便番号は必須です。")
    @Pattern(regexp = "\\d{7}", message = "注文関係者郵便番号は7桁の数字で入力してください。")
    private String orderPartyPostalCode;

    /**
     * 注文関係者都道府県
     */
    @NotBlank(message = "注文関係者都道府県は必須です。")
    @Size(max = 20, message = "注文関係者都道府県は20桁以内で入力してください。")
    private String orderPartyPrefecture;

    /**
     * 注文関係者市区町村
     */
    @NotBlank(message = "注文関係者市区町村は必須です。")
    @Size(max = 100, message = "注文関係者市区町村は100桁以内で入力してください。")
    private String orderPartyCity;

    /**
     * 注文関係者町域
     */
    @Size(max = 100, message = "注文関係者町域は100桁以内で入力してください。")
    private String orderPartyTown;

    /**
     * 注文関係者番地
     */
    @NotBlank(message = "注文関係者番地は必須です。")
    @Size(max = 100, message = "注文関係者番地は100桁以内で入力してください。")
    private String orderPartyStreetAddress;

    /**
     * 注文関係者建物名
     */
    @NotBlank(message = "注文関係者建物名は必須です。")
    @Size(max = 100, message = "注文関係者建物名は100桁以内で入力してください。")
    private String orderPartyBuildingName;
}

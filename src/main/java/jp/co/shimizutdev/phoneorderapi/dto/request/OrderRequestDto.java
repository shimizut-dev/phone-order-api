package jp.co.shimizutdev.phoneorderapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 注文Request DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {

    /**
     * 注文コード
     */
    @NotBlank(message = "注文コードは必須です。")
    @Size(max = 20, message = "注文コードは20桁以内で入力してください。")
    private String orderCode;

    /**
     * 注文日時
     */
    @NotNull(message = "注文日時は必須です。")
    private OffsetDateTime orderedAt;

    /**
     * 注文ステータス
     */
    @NotBlank(message = "注文ステータスは必須です。")
    @Size(max = 10, message = "注文ステータスは10桁以内で入力してください。")
    private String orderStatus;

    /**
     * 注文明細List
     */
    @Valid
    private List<OrderLineRequestDto> orderLines;

    /**
     * 注文関係者List
     */
    @Valid
    private List<OrderPartyRequestDto> orderParties;

    /**
     * 配送List
     */
    @Valid
    private List<DeliveryRequestDto> deliveries;

    /**
     * 注文履歴List
     */
    @Valid
    private List<OrderHistoryRequestDto> orderHistories;
}

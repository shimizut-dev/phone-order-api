package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注文Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDto extends BaseAuditResponseDto {

    /**
     * 注文ID
     */
    private UUID id;

    /**
     * 注文コード
     */
    private String orderCode;

    /**
     * 注文日時
     */
    private OffsetDateTime orderedAt;

    /**
     * 注文ステータス
     */
    private String orderStatus;

    /**
     * 注文明細List
     */
    private List<OrderLineResponseDto> orderLines;

    /**
     * 注文関係者List
     */
    private List<OrderPartyResponseDto> orderParties;

    /**
     * 配送List
     */
    private List<DeliveryResponseDto> deliveries;

    /**
     * 注文履歴List
     */
    private List<OrderHistoryResponseDto> orderHistories;
}
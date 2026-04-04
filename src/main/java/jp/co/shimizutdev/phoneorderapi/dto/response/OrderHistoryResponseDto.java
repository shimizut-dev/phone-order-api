package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注文履歴Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderHistoryResponseDto extends BaseAuditResponseDto {

    /**
     * 注文履歴ID
     */
    private UUID id;

    /**
     * 注文ID
     */
    private UUID orderId;

    /**
     * 注文ステータス
     */
    private String orderStatus;
}
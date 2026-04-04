package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 配送履歴Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class DeliveryHistoryResponseDto extends BaseAuditResponseDto {

    /**
     * 配送履歴ID
     */
    private UUID id;

    /**
     * 配送ID
     */
    private UUID deliveryId;

    /**
     * 配送ステータス
     */
    private String deliveryStatus;
}
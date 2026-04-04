package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 配送明細Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class DeliveryLineResponseDto extends BaseAuditResponseDto {

    /**
     * 配送明細ID
     */
    private UUID id;

    /**
     * 配送ID
     */
    private UUID deliveryId;

    /**
     * 配送明細コード
     */
    private String deliveryLineCode;

    /**
     * 注文明細ID
     */
    private UUID orderLineId;

    /**
     * 配送数量
     */
    private Integer deliveryQuantity;
}
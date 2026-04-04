package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 注文明細Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderLineResponseDto extends BaseAuditResponseDto {

    /**
     * 注文明細ID
     */
    private UUID id;

    /**
     * 注文ID
     */
    private UUID orderId;

    /**
     * 注文明細コード
     */
    private String orderLineCode;

    /**
     * 注文明細区分
     */
    private String orderLineDivision;

    /**
     * 注文数量
     */
    private Integer orderQuantity;

    /**
     * 回線
     */
    private LineResponseDto line;

    /**
     * 移動機
     */
    private PhoneResponseDto phone;

    /**
     * アクセサリ
     */
    private AccessoryResponseDto accessory;
}
package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 移動機Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PhoneResponseDto extends BaseAuditResponseDto {

    /**
     * 移動機ID
     */
    private UUID id;

    /**
     * 注文明細ID
     */
    private UUID orderLineId;

    /**
     * 移動機コード
     */
    private String phoneCode;

    /**
     * IMEI
     */
    private String imei;
}
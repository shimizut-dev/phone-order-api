package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * アクセサリResponse DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class AccessoryResponseDto extends BaseAuditResponseDto {

    /**
     * アクセサリID
     */
    private UUID id;

    /**
     * 注文明細ID
     */
    private UUID orderLineId;

    /**
     * アクセサリコード
     */
    private String accessoryCode;
}
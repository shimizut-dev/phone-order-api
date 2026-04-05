package jp.co.shimizutdev.phoneorderapi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

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
}

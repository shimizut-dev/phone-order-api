package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 回線Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class LineResponseDto extends BaseAuditResponseDto {

    /**
     * 回線ID
     */
    private UUID id;

    /**
     * 注文明細ID
     */
    private UUID orderLineId;

    /**
     * 回線コード
     */
    private String lineCode;

    /**
     * 回線契約区分
     */
    private String lineContractDivision;

    /**
     * MSISDN
     */
    private String msisdn;
}
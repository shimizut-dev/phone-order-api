package jp.co.shimizutdev.phoneorderapi.dto.response;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 監査項目Response DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseAuditResponseDto {

    /**
     * 作成日時
     */
    private OffsetDateTime createdAt;

    /**
     * 作成者
     */
    private String createdBy;

    /**
     * 更新日時
     */
    private OffsetDateTime updatedAt;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 削除日時
     */
    private OffsetDateTime deletedAt;

    /**
     * 削除者
     */
    private String deletedBy;
}
package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.response.BaseAuditResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.BaseAuditEntity;

/**
 * 監査項目Mapper
 */
public final class BaseAuditMapper {

    /**
     * インスタンス化を防止
     */
    private BaseAuditMapper() {
    }

    /**
     * 監査項目をResponse DTOへ設定
     *
     * @param source 変換元エンティティ
     * @param target 変換先Response DTO
     * @return 監査項目を設定したResponse DTO
     */
    public static BaseAuditResponseDto mapToResponse(BaseAuditEntity source, BaseAuditResponseDto target) {
        if (source == null || target == null) {
            return target;
        }

        target.setCreatedAt(source.getCreatedAt());
        target.setCreatedBy(source.getCreatedBy());
        target.setUpdatedAt(source.getUpdatedAt());
        target.setUpdatedBy(source.getUpdatedBy());
        target.setDeletedAt(source.getDeletedAt());
        target.setDeletedBy(source.getDeletedBy());
        return target;
    }
}

package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.AccessoryRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.AccessoryResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.AccessoryEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;

/**
 * アクセサリMapper
 */
public final class AccessoryMapper {

    /**
     * インスタンス化を防止
     */
    private AccessoryMapper() {
    }

    /**
     * アクセサリRequest DTOをアクセサリエンティティへ変換
     *
     * @param dto アクセサリRequest DTO
     * @param orderLine 注文明細
     * @return アクセサリ
     */
    public static AccessoryEntity toEntity(AccessoryRequestDto dto, OrderLineEntity orderLine) {
        if (dto == null) {
            return null;
        }

        AccessoryEntity entity = new AccessoryEntity();
        entity.setOrderLine(orderLine);
        entity.setAccessoryCode(dto.getAccessoryCode());
        return entity;
    }

    /**
     * アクセサリエンティティをアクセサリResponse DTOへ変換
     *
     * @param entity アクセサリ
     * @return アクセサリResponse DTO
     */
    public static AccessoryResponseDto toResponseDto(AccessoryEntity entity) {
        if (entity == null) {
            return null;
        }

        AccessoryResponseDto dto = new AccessoryResponseDto();
        dto.setId(entity.getId());
        dto.setOrderLineId(entity.getOrderLine() != null ? entity.getOrderLine().getId() : null);
        dto.setAccessoryCode(entity.getAccessoryCode());
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }
}

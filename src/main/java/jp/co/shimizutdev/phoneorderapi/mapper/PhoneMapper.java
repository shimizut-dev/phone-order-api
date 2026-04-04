package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.PhoneRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.PhoneResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;
import jp.co.shimizutdev.phoneorderapi.entity.PhoneEntity;

/**
 * 移動機Mapper
 */
public final class PhoneMapper {

    /**
     * インスタンス化を防止
     */
    private PhoneMapper() {
    }

    /**
     * 移動機Request DTOを移動機エンティティへ変換
     *
     * @param dto 移動機Request DTO
     * @param orderLine 注文明細
     * @return 移動機
     */
    public static PhoneEntity toEntity(PhoneRequestDto dto, OrderLineEntity orderLine) {
        if (dto == null) {
            return null;
        }

        PhoneEntity entity = new PhoneEntity();
        entity.setOrderLine(orderLine);
        entity.setPhoneCode(dto.getPhoneCode());
        entity.setImei(dto.getImei());
        return entity;
    }

    /**
     * 移動機エンティティを移動機Response DTOへ変換
     *
     * @param entity 移動機
     * @return 移動機Response DTO
     */
    public static PhoneResponseDto toResponseDto(PhoneEntity entity) {
        if (entity == null) {
            return null;
        }

        PhoneResponseDto dto = new PhoneResponseDto();
        dto.setId(entity.getId());
        dto.setOrderLineId(entity.getOrderLine() != null ? entity.getOrderLine().getId() : null);
        dto.setPhoneCode(entity.getPhoneCode());
        dto.setImei(entity.getImei());
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }
}

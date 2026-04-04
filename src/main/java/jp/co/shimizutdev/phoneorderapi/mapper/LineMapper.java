package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.LineRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.LineResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.LineEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;

/**
 * 回線Mapper
 */
public final class LineMapper {

    /**
     * インスタンス化を防止
     */
    private LineMapper() {
    }

    /**
     * 回線Request DTOを回線エンティティへ変換
     *
     * @param dto 回線Request DTO
     * @param orderLine 注文明細
     * @return 回線
     */
    public static LineEntity toEntity(LineRequestDto dto, OrderLineEntity orderLine) {
        if (dto == null) {
            return null;
        }

        LineEntity entity = new LineEntity();
        entity.setOrderLine(orderLine);
        entity.setLineCode(dto.getLineCode());
        entity.setLineContractDivision(dto.getLineContractDivision());
        entity.setMsisdn(dto.getMsisdn());
        return entity;
    }

    /**
     * 回線エンティティを回線Response DTOへ変換
     *
     * @param entity 回線
     * @return 回線Response DTO
     */
    public static LineResponseDto toResponseDto(LineEntity entity) {
        if (entity == null) {
            return null;
        }

        LineResponseDto dto = new LineResponseDto();
        dto.setId(entity.getId());
        dto.setOrderLineId(entity.getOrderLine() != null ? entity.getOrderLine().getId() : null);
        dto.setLineCode(entity.getLineCode());
        dto.setLineContractDivision(entity.getLineContractDivision());
        dto.setMsisdn(entity.getMsisdn());
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }
}

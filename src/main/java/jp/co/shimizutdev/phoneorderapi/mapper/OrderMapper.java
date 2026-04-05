package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.OrderRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.OrderResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 注文Mapper
 */
public final class OrderMapper {

    /**
     * インスタンス化を防止
     */
    private OrderMapper() {
    }

    /**
     * 注文Request DTOを注文エンティティへ変換
     *
     * @param dto 注文Request DTO
     * @return 注文
     */
    public static OrderEntity toEntity(OrderRequestDto dto) {
        if (dto == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();
        entity.setOrderCode(dto.getOrderCode());
        entity.setOrderedAt(dto.getOrderedAt());
        entity.setOrderStatus(dto.getOrderStatus());
        return entity;
    }

    /**
     * 注文エンティティを注文Response DTOへ変換
     *
     * @param entity 注文
     * @return 注文Response DTO
     */
    public static OrderResponseDto toResponseDto(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(entity.getId());
        dto.setOrderCode(entity.getOrderCode());
        dto.setOrderedAt(entity.getOrderedAt());
        dto.setOrderStatus(entity.getOrderStatus());
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }

    /**
     * 注文Listを注文Response DTO Listへ変換
     *
     * @param entities 注文List
     * @return 注文Response DTO List
     */
    public static List<OrderResponseDto> toResponseDtoList(List<OrderEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(OrderMapper::toResponseDto)
                .filter(Objects::nonNull)
                .toList();
    }
}

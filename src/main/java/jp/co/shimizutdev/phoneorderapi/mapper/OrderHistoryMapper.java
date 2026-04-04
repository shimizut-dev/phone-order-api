package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.OrderHistoryRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.OrderHistoryResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.OrderEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderHistoryEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 注文履歴Mapper
 */
public final class OrderHistoryMapper {

    /**
     * インスタンス化を防止
     */
    private OrderHistoryMapper() {
    }

    /**
     * 注文履歴Request DTOを注文履歴エンティティへ変換
     *
     * @param dto 注文履歴Request DTO
     * @param order 注文
     * @return 注文履歴
     */
    public static OrderHistoryEntity toEntity(OrderHistoryRequestDto dto, OrderEntity order) {
        if (dto == null) {
            return null;
        }

        OrderHistoryEntity entity = new OrderHistoryEntity();
        entity.setOrder(order);
        entity.setOrderStatus(dto.getOrderStatus());
        return entity;
    }

    /**
     * 注文履歴Listを注文履歴エンティティListへ変換
     *
     * @param dtos 注文履歴Request DTO List
     * @param order 注文
     * @return 注文履歴List
     */
    public static List<OrderHistoryEntity> toEntityList(List<OrderHistoryRequestDto> dtos, OrderEntity order) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> toEntity(dto, order))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 注文履歴エンティティを注文履歴Response DTOへ変換
     *
     * @param entity 注文履歴
     * @return 注文履歴Response DTO
     */
    public static OrderHistoryResponseDto toResponseDto(OrderHistoryEntity entity) {
        if (entity == null) {
            return null;
        }

        OrderHistoryResponseDto dto = new OrderHistoryResponseDto();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getId() : null);
        dto.setOrderStatus(entity.getOrderStatus());
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }

    /**
     * 注文履歴Listを注文履歴Response DTO Listへ変換
     *
     * @param entities 注文履歴List
     * @return 注文履歴Response DTO List
     */
    public static List<OrderHistoryResponseDto> toResponseDtoList(List<OrderHistoryEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(OrderHistoryMapper::toResponseDto)
                .filter(Objects::nonNull)
                .toList();
    }
}

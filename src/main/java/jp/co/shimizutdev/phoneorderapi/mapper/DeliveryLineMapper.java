package jp.co.shimizutdev.phoneorderapi.mapper;

import jp.co.shimizutdev.phoneorderapi.dto.request.DeliveryLineRequestDto;
import jp.co.shimizutdev.phoneorderapi.dto.response.DeliveryLineResponseDto;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryEntity;
import jp.co.shimizutdev.phoneorderapi.entity.DeliveryLineEntity;
import jp.co.shimizutdev.phoneorderapi.entity.OrderLineEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 配送明細Mapper
 */
public final class DeliveryLineMapper {

    /**
     * インスタンス化を防止
     */
    private DeliveryLineMapper() {
    }

    /**
     * 配送明細Request DTOを配送明細エンティティへ変換
     *
     * @param dto 配送明細Request DTO
     * @param delivery 配送
     * @return 配送明細
     */
    public static DeliveryLineEntity toEntity(DeliveryLineRequestDto dto, DeliveryEntity delivery) {
        if (dto == null) {
            return null;
        }

        DeliveryLineEntity entity = new DeliveryLineEntity();
        entity.setDelivery(delivery);
        entity.setDeliveryLineCode(dto.getDeliveryLineCode());
        entity.setOrderLine(createOrderLineReference(dto.getOrderLineId()));
        entity.setDeliveryQuantity(dto.getDeliveryQuantity());
        return entity;
    }

    /**
     * 配送明細Listを配送明細エンティティListへ変換
     *
     * @param dtos 配送明細Request DTO List
     * @param delivery 配送
     * @return 配送明細List
     */
    public static List<DeliveryLineEntity> toEntityList(List<DeliveryLineRequestDto> dtos, DeliveryEntity delivery) {
        if (dtos == null) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> toEntity(dto, delivery))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 配送明細エンティティを配送明細Response DTOへ変換
     *
     * @param entity 配送明細
     * @return 配送明細Response DTO
     */
    public static DeliveryLineResponseDto toResponseDto(DeliveryLineEntity entity) {
        if (entity == null) {
            return null;
        }

        DeliveryLineResponseDto dto = new DeliveryLineResponseDto();
        dto.setId(entity.getId());
        dto.setDeliveryId(entity.getDelivery() != null ? entity.getDelivery().getId() : null);
        dto.setDeliveryLineCode(entity.getDeliveryLineCode());
        dto.setOrderLineId(entity.getOrderLine() != null ? entity.getOrderLine().getId() : null);
        dto.setDeliveryQuantity(entity.getDeliveryQuantity());
        BaseAuditMapper.mapToResponse(entity, dto);
        return dto;
    }

    /**
     * 配送明細Listを配送明細Response DTO Listへ変換
     *
     * @param entities 配送明細List
     * @return 配送明細Response DTO List
     */
    public static List<DeliveryLineResponseDto> toResponseDtoList(List<DeliveryLineEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(DeliveryLineMapper::toResponseDto)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 注文明細参照を生成
     *
     * @param orderLineId 注文明細ID
     * @return 注文明細
     */
    private static OrderLineEntity createOrderLineReference(UUID orderLineId) {
        if (orderLineId == null) {
            return null;
        }

        OrderLineEntity orderLine = new OrderLineEntity();
        orderLine.setId(orderLineId);
        return orderLine;
    }
}
